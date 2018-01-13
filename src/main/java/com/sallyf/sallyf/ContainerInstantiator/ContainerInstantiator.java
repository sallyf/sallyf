package com.sallyf.sallyf.ContainerInstantiator;

import com.sallyf.sallyf.Container.*;
import com.sallyf.sallyf.Container.Exception.*;
import com.sallyf.sallyf.Utils.ClassUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ContainerInstantiator
{
    private DependencyTreeFactory dependencyTreeFactory = new DependencyTreeFactory(this);

    private Map<Class, ContainerAwareInterface> services;

    private Map<String, ArrayList<ContainerAwareInterface>> taggedServices;

    private HashMap<Class, ServiceDefinition<? extends ContainerAwareInterface>> serviceDefinitions = new HashMap<>();

    private ArrayList<ReferenceResolverInterface> referenceResolvers = new ArrayList<>();

    private ArrayList<TypeResolverInterface> typeResolvers = new ArrayList<>();

    private Map<Class, ConfigurationInterface> configurations = new HashMap<>();

    private HashMap<Class, ServiceDefinitionMeta> serviceDefinitionMetas = new HashMap<>();

    public ContainerInstantiator(Map<Class, ContainerAwareInterface> services, Map<String, ArrayList<ContainerAwareInterface>> taggedServices)
    {
        this.services = services;
        this.taggedServices = taggedServices;
    }

    public void boot()
    {
        while (!allServiceDefinitionMetasReady()) {
            ChangeTracker ct = new ChangeTracker();

            for (ServiceDefinitionMeta<?> serviceDefinitionMeta : getNotReadyServiceDefinitionMetas()) {
                ServiceDefinition<?> serviceDefinition = serviceDefinitionMeta.getServiceDefinition();

                ContainerAwareInterface instance;

                if (!serviceDefinitionMeta.isInstantiated()) {
                    DependencyTree dependenciesTree = getServiceDependenciesTree(serviceDefinition);

                    if (!dependenciesTree.isFullyInstantiable()) {
                        throw new ReferenceResolutionException("Service " + serviceDefinition.getType() + " is not instantiable due to missing dependency");
                    }

                    if (dependenciesTree.hasCircularReference()) {
                        throw new CircularReferenceException(dependenciesTree.getCircularReferencePath());
                    }

                    if (!dependenciesTree.isFullyInstantiated()) {
                        continue;
                    }

                    instance = bootService(serviceDefinitionMeta);

                    ct.apply(true);
                } else {
                    instance = services.get(serviceDefinition.getAlias());
                }

                ct.apply(updateServiceDefinitionMetasMethodCallDefinitionMetas());

                ct.apply(invokeCalls());

                ct.apply(updateServiceDefinitionMetasMethodCallDefinitionMetas());

                if (serviceDefinitionMeta.isFullyInstantiated() && !serviceDefinitionMeta.isInitialized()) {
                    try {
                        instance.initialize();
                    } catch (Exception e) {
                        throw new ServiceInstantiationException(e);
                    }

                    updateServiceDefinitionMetasMethodCallDefinitionMetas();

                    serviceDefinitionMeta.setInitialized(true);
                    ct.apply(true);
                }
            }

            if (!ct.isChanged() && !allServiceDefinitionMetasReady()) {
                throw new ServiceInstantiationException("Unable to instantiate all services");
            }
        }

        if (!allMethodCallDefinitionsCalled()) {
            throw new ServiceInstantiationException("All method calls weren't called, probably because of a missing dependency");
        }
    }

    private boolean updateServiceDefinitionMetasMethodCallDefinitionMetas()
    {
        ChangeTracker ct = new ChangeTracker();

        for (Map.Entry<Class, ServiceDefinitionMeta> entry : serviceDefinitionMetas.entrySet()) {
            ct.apply(entry.getValue().updateMethodCallDefinitionMetas());
        }

        return ct.isChanged();
    }

    private List<ServiceDefinitionMeta> getNotReadyServiceDefinitionMetas()
    {
        return serviceDefinitionMetas.entrySet().stream()
                .map(Map.Entry::getValue)
                .filter(m -> !m.isReady())
                .collect(Collectors.toList());
    }

    private boolean allServiceDefinitionMetasReady()
    {
        return getNotReadyServiceDefinitionMetas().isEmpty();
    }

    private List<MethodCallDefinitionMeta> getUncalledMethodCallDefinitionMetas()
    {
        return serviceDefinitionMetas.entrySet().stream()
                .map(Map.Entry::getValue)
                .flatMap(m -> (Stream<MethodCallDefinitionMeta>) m.getMethodCallDefinitionMetas().stream())
                .filter(m -> !m.isCalled())
                .collect(Collectors.toList());
    }

    private boolean allMethodCallDefinitionsCalled()
    {
        return getUncalledMethodCallDefinitionMetas().isEmpty();
    }

    private <T extends ContainerAwareInterface> DependencyTree<T> getServiceDependenciesTree(ServiceDefinition<T> serviceDefinition)
    {
        return dependencyTreeFactory.generate(serviceDefinition);
    }

    public <T extends ContainerAwareInterface> void autoWire(ServiceDefinitionMeta<T> serviceDefinitionMeta)
    {
        ServiceDefinition<T> serviceDefinition = serviceDefinitionMeta.getServiceDefinition();

        if (!serviceDefinition.isAutoWire()) {
            serviceDefinitionMeta.setWired(true);
        }

        if (!serviceDefinitionMeta.isWired()) {
            Constructor<?>[] constructors = serviceDefinition.getType().getConstructors();

            for (Constructor<?> constructor : constructors) {
                Class<?>[] parameterTypes = constructor.getParameterTypes();

                ReferenceInterface[] references = resolveTypes(serviceDefinition, parameterTypes);

                serviceDefinition.addConstructorDefinition(new ConstructorDefinition(references));
            }

            serviceDefinitionMeta.setWired(true);
        }
    }

    private <T extends ContainerAwareInterface> T bootService(ServiceDefinitionMeta<T> serviceDefinitionMeta)
    {
        ServiceDefinition<T> serviceDefinition = serviceDefinitionMeta.getServiceDefinition();

        Class<T> serviceClass = serviceDefinition.getType();

        T instance = instantiateService(serviceDefinition);

        if (null == instance) {
            throw new ServiceInstantiationException("Unable to boot service " + serviceClass);
        }

        services.put(serviceDefinition.getAlias(), instance);

        for (String tag : serviceDefinition.getTags()) {
            addTaggedService(tag, instance);
        }

        serviceDefinitionMeta.setInstantiated(true);

        return instance;
    }

    private <T extends ContainerAwareInterface> T instantiateService(ServiceDefinition<T> serviceDefinition)
    {
        Class<T> serviceClass = serviceDefinition.getType();

        T instance = null;

        for (ConstructorDefinition constructorDefinition : serviceDefinition.getConstructorDefinitions()) {
            try {
                Object[] args = resolveReferences(serviceDefinition, constructorDefinition.getArgs());
                Constructor<T> constructor = ClassUtils.getConstructorForArgs(serviceClass, ClassUtils.getClasses(args));
                if (constructor != null) {
                    instance = constructor.newInstance(args);
                    break;
                }
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                throw new ServiceInstantiationException(e);
            }
        }

        return instance;
    }

    private boolean invokeCalls()
    {
        ChangeTracker ct = new ChangeTracker();

        for (MethodCallDefinitionMeta methodCallDefinitionMeta : getUncalledMethodCallDefinitionMetas()) {
            ServiceDefinition<?> serviceDefinition = methodCallDefinitionMeta.getServiceDefinitionMeta().getServiceDefinition();

            MethodCallDefinition methodCallDefinition = methodCallDefinitionMeta.getMethodCallDefinition();

            if (!isReadyToBeCalled(methodCallDefinition)) {
                continue;
            }

            ContainerAwareInterface instance = services.get(serviceDefinition.getAlias());

            try {
                Object[] args = resolveReferences(serviceDefinition, methodCallDefinition.getArgs());

                Method method = serviceDefinition.getType().getMethod(methodCallDefinition.getName(), ClassUtils.getClasses(args));

                method.invoke(instance, args);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new MethodCallException(e);
            }

            methodCallDefinitionMeta.setCalled(true);
            ct.apply(true);
        }

        return ct.isChanged();
    }

    private boolean isReadyToBeCalled(MethodCallDefinition methodCallDefinition)
    {
        ReferenceInterface[] references = methodCallDefinition.getArgs();

        for (ReferenceInterface reference : references) {
            if (reference instanceof ServiceReference) {
                ServiceReference<?> serviceReference = (ServiceReference<?>) reference;

                Class<?> alias = serviceReference.getAlias();

                if (!services.containsKey(alias)) {
                    if (!serviceDefinitions.containsKey(alias)) {
                        throw new ServiceInstantiationException("Unknown service: " + alias.getName());
                    }

                    return false;
                }
            }
        }

        return true;
    }

    private Object[] resolveReferences(ServiceDefinition serviceDefinition, ReferenceInterface[] references)
    {
        Object[] args = new Object[references.length];

        int i = 0;
        for (ReferenceInterface reference : references) {
            args[i++] = resolveReference(serviceDefinition, reference);
        }

        return args;
    }

    private Object resolveReference(ServiceDefinition serviceDefinition, ReferenceInterface reference)
    {
        for (ReferenceResolverInterface resolver : referenceResolvers) {
            if (resolver.supports(serviceDefinition, reference)) {
                try {
                    return resolver.resolve(serviceDefinition, reference);
                } catch (java.lang.Exception e) {
                    throw new ReferenceResolutionException(e);
                }
            }
        }

        throw new ReferenceResolutionException("Unhandled reference type: " + reference);
    }

    private ReferenceInterface[] resolveTypes(ServiceDefinition serviceDefinition, Class[] types)
    {
        ReferenceInterface[] references = new ReferenceInterface[types.length];

        int i = 0;
        for (Class<?> type : types) {
            references[i++] = resolveType(serviceDefinition, type);
        }

        return references;
    }

    private ReferenceInterface resolveType(ServiceDefinition serviceDefinition, Class type)
    {
        for (TypeResolverInterface resolver : typeResolvers) {
            if (resolver.supports(serviceDefinition, type)) {
                try {
                    return resolver.resolve(serviceDefinition, type);
                } catch (java.lang.Exception e) {
                    throw new TypeResolutionException(e);
                }
            }
        }

        throw new TypeResolutionException("Unable to auto wire reference for " + type);
    }

    private void addTaggedService(String tag, ContainerAwareInterface service)
    {
        if (!taggedServices.containsKey(tag)) {
            taggedServices.put(tag, new ArrayList<>());
        }

        taggedServices.get(tag).add(service);
    }

    public <T extends ContainerAwareInterface> void addServiceDefinition(ServiceDefinition<T> serviceDefinition)
    {
        serviceDefinitions.put(serviceDefinition.getAlias(), serviceDefinition);

        ServiceDefinitionMeta<T> serviceDefinitionMeta = new ServiceDefinitionMeta<>(serviceDefinition);
        serviceDefinitionMetas.put(serviceDefinition.getAlias(), serviceDefinitionMeta);

        autoWire(serviceDefinitionMeta);
    }

    public Map<Class, ServiceDefinition<? extends ContainerAwareInterface>> getServiceDefinitions()
    {
        return serviceDefinitions;
    }

    public ArrayList<ReferenceResolverInterface> getReferenceResolvers()
    {
        return referenceResolvers;
    }

    public ArrayList<TypeResolverInterface> getTypeResolvers()
    {
        return typeResolvers;
    }

    public Map<Class, ConfigurationInterface> getConfigurations()
    {
        return configurations;
    }

    public Map<Class, ContainerAwareInterface> getServices()
    {
        return services;
    }
}
