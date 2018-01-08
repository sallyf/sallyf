package com.sallyf.sallyf.Container;

import com.sallyf.sallyf.Container.Exception.*;
import com.sallyf.sallyf.Utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

class CallDefinitionMeta<T extends ContainerAwareInterface>
{
    public CallDefinition callDefinition;

    public ServiceDefinition<T> serviceDefinition;

    public CallDefinitionMeta(CallDefinition callDefinition, ServiceDefinition<T> serviceDefinition)
    {
        this.callDefinition = callDefinition;
        this.serviceDefinition = serviceDefinition;
    }
}

class ContainerInstantiator
{
    private Map<Class, ServiceDefinition<? extends ContainerAwareInterface>> serviceDefinitions = new HashMap<>();

    private Map<Class, ContainerAwareInterface> services;

    private ArrayList<ReferenceResolverInterface> referenceResolvers = new ArrayList<>();

    private ArrayList<TypeResolverInterface> typeResolvers = new ArrayList<>();

    private Map<Class, ConfigurationInterface> configurations = new HashMap<>();

    private ArrayList<ServiceDefinition> autoWiredDefinitions = new ArrayList<>();

    private ArrayList<CallDefinitionMeta> callDefinitionMetas = new ArrayList<>();

    ContainerInstantiator(Map<Class, ContainerAwareInterface> services)
    {
        this.services = services;
    }

    public Map<Class, ContainerAwareInterface> boot() throws ServiceInstantiationException
    {
        for (Map.Entry<Class, ServiceDefinition<? extends ContainerAwareInterface>> entry : serviceDefinitions.entrySet()) {
            ServiceDefinition<? extends ContainerAwareInterface> serviceDefinition = entry.getValue();
            ArrayList<CallDefinition> callDefinitions = serviceDefinition.getCallDefinitions();

            for (CallDefinition callDefinition : callDefinitions) {
                callDefinitionMetas.add(new CallDefinitionMeta<>(callDefinition, serviceDefinition));
            }
        }

        Map<Class, ServiceDefinition<? extends ContainerAwareInterface>> serviceDefinitionsPool = new HashMap<>(this.serviceDefinitions);

        while (!serviceDefinitionsPool.isEmpty()) {
            boolean hasInstantiated = false;

            Set<Map.Entry<Class, ServiceDefinition<? extends ContainerAwareInterface>>> entries = new HashSet<>(serviceDefinitionsPool.entrySet());

            for (Map.Entry<Class, ServiceDefinition<? extends ContainerAwareInterface>> entry : entries) {
                ServiceDefinition<? extends ContainerAwareInterface> serviceDefinition = entry.getValue();

                autoWire(serviceDefinition);

                try {
                    bootService(serviceDefinition);
                    hasInstantiated = true;
                } catch (ServiceInstantiationException e) {
                    continue;
                }

                invokeCalls();

                serviceDefinitionsPool.remove(entry.getKey());
            }

            if (!entries.isEmpty() && !hasInstantiated) {
                throw new CircularReferenceException();
            }
        }

        if (!callDefinitionMetas.isEmpty()) {
            throw new ServiceInstantiationException("All calls weren't called");
        }

        return services;
    }

    private <T extends ContainerAwareInterface> void autoWire(ServiceDefinition<T> serviceDefinition) throws ServiceInstantiationException
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

    private <T extends ContainerAwareInterface> void bootService(ServiceDefinition<T> serviceDefinition) throws ServiceInstantiationException
    {
        Class<T> serviceClass = serviceDefinition.getType();

        T instance = instantiateService(serviceDefinition);

        if (null == instance) {
            throw new ServiceInstantiationException("Unable to boot service " + serviceClass);
        }

        services.put(serviceDefinition.getAlias(), instance);

        try {
            instance.initialize();
        } catch (Exception e) {
            throw new ServiceInstantiationException(e);
        }
    }

    private <T extends ContainerAwareInterface> T instantiateService(ServiceDefinition<T> serviceDefinition) throws ServiceInstantiationException
    {
        Class<T> serviceClass = serviceDefinition.getType();

        T instance = null;

        for (ConstructorDefinition constructorDefinition : serviceDefinition.getConstructorDefinitions()) {
            try {
                Object[] args = resolveReferences(serviceDefinition, constructorDefinition.args);
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
        ArrayList<CallDefinitionMeta> callDefinitionMetas = new ArrayList<>(this.callDefinitionMetas);

        for (CallDefinitionMeta callDefinitionMeta : callDefinitionMetas) {
            ServiceDefinition<?> serviceDefinition = callDefinitionMeta.serviceDefinition;
            CallDefinition callDefinition = callDefinitionMeta.callDefinition;

            ContainerAwareInterface instance = services.get(serviceDefinition.getAlias());

            try {
                Object[] args = resolveReferences(serviceDefinition, callDefinition.args);

                Method method = serviceDefinition.getType().getMethod(callDefinition.name, Utils.getClasses(args));

                method.invoke(instance, args);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new CallException(e);
            } catch (ReferenceResolutionException ignored) {
                continue;
            }

            this.callDefinitionMetas.remove(callDefinitionMeta);
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
}
