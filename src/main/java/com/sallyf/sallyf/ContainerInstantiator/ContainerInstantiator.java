package com.sallyf.sallyf.ContainerInstantiator;

import com.sallyf.sallyf.Container.*;
import com.sallyf.sallyf.Container.Exception.*;
import com.sallyf.sallyf.Utils;

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

    private ArrayList<ServiceDefinition> autoWiredDefinitions = new ArrayList<>();

    private ArrayList<ServiceDefinitionMeta> serviceDefinitionMetas = new ArrayList<>();

    public ContainerInstantiator(Map<Class, ContainerAwareInterface> services, Map<String, ArrayList<ContainerAwareInterface>> taggedServices)
    {
        this.services = services;
        this.taggedServices = taggedServices;
    }

    public void boot() throws ServiceInstantiationException
    {
        while (!allServiceDefinitionsInstantiated()) {
            boolean hasInstantiated = false;

            for (ServiceDefinitionMeta<?> serviceDefinitionMeta : getUninstantiatedServiceDefinitionMetas()) {
                ServiceDefinition<?> serviceDefinition = serviceDefinitionMeta.getServiceDefinition();

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

                bootService(serviceDefinitionMeta);

                updateServiceDefinitionMetasCallDefinitionMetas();

                invokeCalls();

                hasInstantiated = true;
            }

            if (!hasInstantiated && !allServiceDefinitionsInstantiated()) {
                throw new ServiceInstantiationException("Unable to instantiate all services");
            }
        }

        invokeCalls();

        if (!allCallDefinitionsCalled()) {
            throw new ServiceInstantiationException("All calls weren't called");
        }

        for (Map.Entry<Class, ContainerAwareInterface> entry : this.services.entrySet()) {
            try {
                entry.getValue().initialize();
            } catch (Exception e) {
                throw new ServiceInstantiationException(e);
            }
        }
    }

    private void updateServiceDefinitionMetasCallDefinitionMetas()
    {
        for (ServiceDefinitionMeta serviceDefinitionMeta : serviceDefinitionMetas) {
            serviceDefinitionMeta.updateCallDefinitionMetas();
        }
    }

    private List<ServiceDefinitionMeta> getUninstantiatedServiceDefinitionMetas()
    {
        return serviceDefinitionMetas.stream().filter(m -> !m.isInstantiated()).collect(Collectors.toList());
    }

    private boolean allServiceDefinitionsInstantiated()
    {
        return getUninstantiatedServiceDefinitionMetas().isEmpty();
    }

    private List<CallDefinitionMeta> getUncalledCallDefinitionMeta()
    {
        return serviceDefinitionMetas.stream()
                .flatMap(m -> (Stream<CallDefinitionMeta>) m.getCallDefinitionMetas().stream())
                .filter(o -> !o.isCalled())
                .collect(Collectors.toList());
    }

    private boolean allCallDefinitionsCalled()
    {
        return getUncalledCallDefinitionMeta().isEmpty();
    }

    private <T extends ContainerAwareInterface> DependencyTree<T> getServiceDependenciesTree(ServiceDefinition<T> serviceDefinition) throws ServiceInstantiationException
    {
        return dependencyTreeFactory.generate(serviceDefinition);
    }

    public <T extends ContainerAwareInterface> void autoWire(ServiceDefinition<T> serviceDefinition) throws ServiceInstantiationException
    {
        if (serviceDefinition.isAutoWire() && !autoWiredDefinitions.contains(serviceDefinition)) {
            Constructor<?>[] constructors = serviceDefinition.getType().getConstructors();

            for (Constructor<?> constructor : constructors) {
                Class<?>[] parameterTypes = constructor.getParameterTypes();

                ReferenceInterface[] references = resolveTypes(serviceDefinition, parameterTypes);

                serviceDefinition.addConstructorDefinition(new ConstructorDefinition(references));
            }

            autoWiredDefinitions.add(serviceDefinition);
        }
    }

    private <T extends ContainerAwareInterface> T bootService(ServiceDefinitionMeta<T> serviceDefinitionMeta) throws ServiceInstantiationException
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

    private <T extends ContainerAwareInterface> T instantiateService(ServiceDefinition<T> serviceDefinition) throws ServiceInstantiationException
    {
        Class<T> serviceClass = serviceDefinition.getType();

        T instance = null;

        for (ConstructorDefinition constructorDefinition : serviceDefinition.getConstructorDefinitions()) {
            try {
                Object[] args = resolveReferences(serviceDefinition, constructorDefinition.getArgs());
                Constructor<T> constructor = Utils.getConstructorForArgs(serviceClass, Utils.getClasses(args));
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

    private void invokeCalls() throws CallException
    {
        for (CallDefinitionMeta callDefinitionMeta : getUncalledCallDefinitionMeta()) {
            ServiceDefinition<?> serviceDefinition = callDefinitionMeta.getServiceDefinitionMeta().getServiceDefinition();

            CallDefinition callDefinition = callDefinitionMeta.getCallDefinition();

            ContainerAwareInterface instance = services.get(serviceDefinition.getAlias());

            try {
                Object[] args = resolveReferences(serviceDefinition, callDefinition.getArgs());

                Method method = serviceDefinition.getType().getMethod(callDefinition.getName(), Utils.getClasses(args));

                method.invoke(instance, args);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new CallException(e);
            } catch (ReferenceResolutionException ignored) {
                continue;
            }

            callDefinitionMeta.setCalled(true);
        }
    }

    private Object[] resolveReferences(ServiceDefinition serviceDefinition, ReferenceInterface[] references) throws ReferenceResolutionException
    {
        Object[] args = new Object[references.length];

        int i = 0;
        for (ReferenceInterface reference : references) {
            args[i++] = resolveReference(serviceDefinition, reference);
        }

        return args;
    }

    private Object resolveReference(ServiceDefinition serviceDefinition, ReferenceInterface reference) throws ReferenceResolutionException
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

    private ReferenceInterface[] resolveTypes(ServiceDefinition serviceDefinition, Class[] types) throws TypeResolutionException
    {
        ReferenceInterface[] references = new ReferenceInterface[types.length];

        int i = 0;
        for (Class<?> type : types) {
            references[i++] = resolveType(serviceDefinition, type);
        }

        return references;
    }

    private ReferenceInterface resolveType(ServiceDefinition serviceDefinition, Class type) throws TypeResolutionException
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

    public <T extends ContainerAwareInterface> void addServiceDefinition(ServiceDefinition<T> serviceDefinition) throws ServiceInstantiationException
    {
        serviceDefinitions.put(serviceDefinition.getAlias(), serviceDefinition);

        serviceDefinitionMetas.add(new ServiceDefinitionMeta<>(serviceDefinition));

        autoWire(serviceDefinition);
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
