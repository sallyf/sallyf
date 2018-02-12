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

public class ContainerInstantiator
{
    private Container container;

    private DependencyTreeFactory dependencyTreeFactory = new DependencyTreeFactory(this);

    private Map<Class, ServiceInterface> services;

    private Map<String, ArrayList<ServiceInterface>> taggedServices;

    private HashMap<Class, ServiceDefinition<? extends ServiceInterface>> serviceDefinitions = new HashMap<>();

    private ArrayList<ReferenceResolverInterface> referenceResolvers = new ArrayList<>();

    private ArrayList<TypeResolverInterface> typeResolvers = new ArrayList<>();

    private Map<Class, ConfigurationInterface> configurations = new HashMap<>();

    private HashMap<Class, ServiceDefinitionMeta<?>> serviceDefinitionMetas = new HashMap<>();

    public ContainerInstantiator(Container container, Map<Class, ServiceInterface> services, Map<String, ArrayList<ServiceInterface>> taggedServices)
    {
        this.container = container;
        this.services = services;
        this.taggedServices = taggedServices;
    }

    public void boot()
    {
        if (container.isInstantiated()) {
            throw new ContainerInstantiatedException();
        }

        serviceDefinitionMetas.values().forEach(this::autoWire);

        while (!allServiceDefinitionMetasReady()) {
            ChangeTracker ct = new ChangeTracker();

            for (ServiceDefinitionMeta<?> serviceDefinitionMeta : getNotReadyServiceDefinitionMetas()) {
                boot(ct, serviceDefinitionMeta);
            }

            if (!ct.isChanged() && !allServiceDefinitionMetasReady()) {
                throw new ServiceInstantiationException("Unable to instantiate all services");
            }
        }
    }

    private <T extends ServiceInterface> void boot(ChangeTracker ct, ServiceDefinitionMeta<T> serviceDefinitionMeta)
    {
        ServiceDefinition<?> serviceDefinition = serviceDefinitionMeta.getServiceDefinition();

        ServiceInterface instance;

        if (!serviceDefinitionMeta.isInstantiated()) {
            DependencyTree dependenciesTree = getServiceDependenciesTree(serviceDefinition);

            if (!dependenciesTree.isFullyInstantiable()) {
                throw new ReferenceResolutionException("Service " + serviceDefinition.getType() + " is not instantiable due to missing dependency");
            }

            if (dependenciesTree.hasCircularReference()) {
                throw new CircularReferenceException(dependenciesTree.getCircularReferencePath());
            }

            if (!dependenciesTree.isFullyInstantiated()) {
                return;
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
                instance.initialize(container);
            } catch (Exception e) {
                throw new ServiceInstantiationException(e);
            }

            updateServiceDefinitionMetasMethodCallDefinitionMetas();

            serviceDefinitionMeta.setInitialized(true);
            ct.apply(true);
        }
    }

    private boolean updateServiceDefinitionMetasMethodCallDefinitionMetas()
    {
        ChangeTracker ct = new ChangeTracker();

        for (ServiceDefinitionMeta serviceDefinitionMeta : serviceDefinitionMetas.values()) {
            ct.apply(serviceDefinitionMeta.updateMethodCallDefinitionMetas());
        }

        return ct.isChanged();
    }

    private List<ServiceDefinitionMeta> getNotReadyServiceDefinitionMetas()
    {
        return serviceDefinitionMetas.values().stream()
                .filter(m -> !m.isReady())
                .collect(Collectors.toList());
    }

    private boolean allServiceDefinitionMetasReady()
    {
        return getNotReadyServiceDefinitionMetas().isEmpty();
    }

    private List<MethodCallDefinitionMeta> getUncalledMethodCallDefinitionMetas()
    {
        return serviceDefinitionMetas.values().stream()
                .flatMap(m -> m.getMethodCallDefinitionMetas().stream())
                .filter(m -> !m.isCalled())
                .collect(Collectors.toList());
    }

    private boolean allMethodCallDefinitionsCalled()
    {
        return getUncalledMethodCallDefinitionMetas().isEmpty();
    }

    private <T extends ServiceInterface> DependencyTree<T> getServiceDependenciesTree(ServiceDefinition<T> serviceDefinition)
    {
        return dependencyTreeFactory.generate(serviceDefinition);
    }

    public <T extends ServiceInterface> void autoWire(ServiceDefinitionMeta<T> serviceDefinitionMeta)
    {
        ServiceDefinition<T> serviceDefinition = serviceDefinitionMeta.getServiceDefinition();

        if (serviceDefinition.isAutoWire() && !serviceDefinitionMeta.isWired()) {
            Constructor<?>[] constructors = serviceDefinition.getType().getConstructors();

            if (constructors.length != 1) {
                throw new ServiceInstantiationException("Ambiguous constructor in: " + serviceDefinition.getAlias().getName());
            }

            Constructor<?> constructor = constructors[0];

            Class<?>[] parameterTypes = constructor.getParameterTypes();

            ReferenceInterface[] references = resolveTypes(serviceDefinition, parameterTypes);

            serviceDefinition.setConstructorDefinition(new ConstructorDefinition(references));
        }

        serviceDefinitionMeta.setWired(true);
    }

    private <T extends ServiceInterface> T bootService(ServiceDefinitionMeta<T> serviceDefinitionMeta)
    {
        ServiceDefinition<T> serviceDefinition = serviceDefinitionMeta.getServiceDefinition();

        T instance = instantiateService(serviceDefinition);

        services.put(serviceDefinition.getAlias(), instance);

        for (String tag : serviceDefinition.getTags()) {
            addTaggedService(tag, instance);
        }

        serviceDefinitionMeta.setInstantiated(true);

        return instance;
    }

    private <T extends ServiceInterface> T instantiateService(ServiceDefinition<T> serviceDefinition)
    {
        Class<T> serviceClass = serviceDefinition.getType();

        ConstructorDefinition constructorDefinition = serviceDefinition.getConstructorDefinition();

        if (null == constructorDefinition) {
            throw new ServiceInstantiationException("No constructor available");
        }

        try {
            Object[] args = resolveReferences(serviceDefinition, constructorDefinition.getArgs());
            Constructor<T> constructor = ClassUtils.getConstructorForArgs(serviceClass, ClassUtils.getClasses(args));
            if (constructor != null) {
                return constructor.newInstance(args);
            }
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new ServiceInstantiationException(e);
        }

        throw new ServiceInstantiationException("Unable to instantiate service " + serviceClass);
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

            ServiceInterface instance = services.get(serviceDefinition.getAlias());

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

    private Object resolveReference(ServiceDefinition<?> serviceDefinition, ReferenceInterface reference)
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

    private void addTaggedService(String tag, ServiceInterface service)
    {
        if (!taggedServices.containsKey(tag)) {
            taggedServices.put(tag, new ArrayList<>());
        }

        taggedServices.get(tag).add(service);
    }

    public <T extends ServiceInterface> void addServiceDefinition(ServiceDefinition<T> serviceDefinition)
    {
        if (container.isInstantiated()) {
            throw new ContainerInstantiatedException();
        }

        serviceDefinitions.put(serviceDefinition.getAlias(), serviceDefinition);

        ServiceDefinitionMeta<T> serviceDefinitionMeta = new ServiceDefinitionMeta<>(serviceDefinition);
        serviceDefinitionMetas.put(serviceDefinition.getAlias(), serviceDefinitionMeta);

        if (container.isInstantiating()) {
            autoWire(serviceDefinitionMeta);
        }
    }

    public Map<Class, ServiceDefinition<? extends ServiceInterface>> getServiceDefinitions()
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

    public Map<Class, ServiceInterface> getServices()
    {
        return services;
    }
}
