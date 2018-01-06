package com.sallyf.sallyf.Container;

import com.sallyf.sallyf.Container.Exception.CircularReferenceException;
import com.sallyf.sallyf.Container.Exception.ContainerInstantiatedException;
import com.sallyf.sallyf.Container.Exception.ReferenceResolutionException;
import com.sallyf.sallyf.Container.Exception.ServiceInstantiationException;
import com.sallyf.sallyf.Container.ReferenceResolver.ConfigurationReferenceResolver;
import com.sallyf.sallyf.Container.ReferenceResolver.ContainerReferenceResolver;
import com.sallyf.sallyf.Container.ReferenceResolver.PlainReferenceResolver;
import com.sallyf.sallyf.Container.ReferenceResolver.ServiceReferenceResolver;
import com.sallyf.sallyf.Container.TypeResolver.ConfigurationResolver;
import com.sallyf.sallyf.Container.TypeResolver.ContainerResolver;
import com.sallyf.sallyf.Container.TypeResolver.ServiceResolver;
import com.sallyf.sallyf.Utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Container
{
    private Map<Class, ContainerAwareInterface> services;

    private Map<Class, ConfigurationInterface> configurations;

    private Map<Class, ServiceDefinition<? extends ContainerAwareInterface>> serviceDefinitions;

    private ArrayList<ReferenceResolverInterface> referenceResolvers;

    private ArrayList<TypeResolverInterface> typeResolvers;

    private boolean instantiated = false;

    public Container()
    {
        services = new HashMap<>();
        serviceDefinitions = new HashMap<>();
        configurations = new HashMap<>();
        referenceResolvers = new ArrayList<>();
        typeResolvers = new ArrayList<>();

        referenceResolvers.add(new ConfigurationReferenceResolver(this));
        referenceResolvers.add(new ContainerReferenceResolver(this));
        referenceResolvers.add(new PlainReferenceResolver());
        referenceResolvers.add(new ServiceReferenceResolver(this));

        typeResolvers.add(new ConfigurationResolver());
        typeResolvers.add(new ContainerResolver());
        typeResolvers.add(new ServiceResolver());
    }

    public void addAll(ServiceDefinition<? extends ContainerAwareInterface>[] serviceDefinitions) throws ServiceInstantiationException
    {
        for (ServiceDefinition<? extends ContainerAwareInterface> serviceDefinition : serviceDefinitions) {
            add(serviceDefinition);
        }
    }

    public <T extends ContainerAwareInterface> ServiceDefinition<T> add(ServiceDefinition<T> serviceDefinition) throws ServiceInstantiationException
    {
        if (instantiated) {
            throw new ContainerInstantiatedException();
        }

        serviceDefinitions.put(serviceDefinition.getAlias(), serviceDefinition);

        return serviceDefinition;
    }

    public void instantiateServices() throws ServiceInstantiationException
    {
        if (instantiated) {
            throw new ContainerInstantiatedException();
        }

        Map<Class, ServiceDefinition<? extends ContainerAwareInterface>> serviceDefinitions = new HashMap<>(this.serviceDefinitions);

        ArrayList<ServiceDefinition> autoWiredDefinitions = new ArrayList<>();

        while (!serviceDefinitions.isEmpty()) {
            boolean hasInstantiated = false;

            Set<Map.Entry<Class, ServiceDefinition<? extends ContainerAwareInterface>>> entries = new HashSet<>(serviceDefinitions.entrySet());

            for (Map.Entry<Class, ServiceDefinition<? extends ContainerAwareInterface>> entry : entries) {
                ServiceDefinition<? extends ContainerAwareInterface> serviceDefinition = entry.getValue();

                if (serviceDefinition.isAutoWire() && !autoWiredDefinitions.contains(serviceDefinition)) {
                    Constructor<?>[] constructors = serviceDefinition.getType().getConstructors();

                    for (Constructor<?> constructor : constructors) {
                        Class<?>[] parameterTypes = constructor.getParameterTypes();

                        ReferenceInterface[] references = resolveReferencesFromType(serviceDefinition, parameterTypes);

                        serviceDefinition.addConstructorDefinition(new ConstructorDefinition(references));
                    }

                    autoWiredDefinitions.add(serviceDefinition);
                }

                try {
                    instantiateService(serviceDefinition);
                    hasInstantiated = true;
                } catch (ReferenceResolutionException e) {
                    continue;
                }

                serviceDefinitions.remove(entry.getKey());
            }

            if (!hasInstantiated) {
                throw new CircularReferenceException();
            }
        }

        instantiated = true;
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
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException ignored) {
            }
        }

        if (null == instance) {
            throw new ServiceInstantiationException("Unable to instantiate service " + serviceClass);
        }

        for (CallDefinition callDefinition : serviceDefinition.getCallDefinitions()) {
            Object[] args = resolveReferences(serviceDefinition, callDefinition.args);

            try {
                Method method = serviceClass.getMethod(callDefinition.name, Utils.getClasses(args));

                method.invoke(instance, args);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new ServiceInstantiationException(e);
            }
        }

        services.put(serviceDefinition.getAlias(), instance);

        try {
            instance.initialize();
        } catch (Exception e) {
            throw new ServiceInstantiationException(e);
        }

        return instance;
    }

    private ReferenceInterface[] resolveReferencesFromType(ServiceDefinition serviceDefinition, Class[] types) throws ServiceInstantiationException
    {
        ReferenceInterface[] references = new ReferenceInterface[types.length];

        int i = 0;
        for (Class<?> type : types) {
            references[i++] = resolveReferenceFromType(serviceDefinition, type);
        }

        return references;
    }

    private ReferenceInterface resolveReferenceFromType(ServiceDefinition serviceDefinition, Class type) throws ServiceInstantiationException
    {
        for (TypeResolverInterface resolver : typeResolvers) {
            if (resolver.supports(serviceDefinition, type)) {
                try {
                    return resolver.resolve(serviceDefinition, type);
                } catch (Exception e) {
                    throw new ServiceInstantiationException(e);
                }
            }
        }

        throw new ServiceInstantiationException("Unable to auto wire reference for " + type);
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
                return resolver.resolve(serviceDefinition, reference);
            }
        }

        throw new ReferenceResolutionException("Unhandled reference type: " + reference);
    }

    public <T extends ContainerAwareInterface> T get(Class<T> serviceClass)
    {
        return (T) services.get(serviceClass);
    }

    public <T extends ContainerAwareInterface, C> C get(Class<T> serviceClass, Class<C> castType)
    {
        return (C) get(serviceClass);
    }

    public boolean has(Class serviceClass)
    {
        return services.containsKey(serviceClass);
    }

    public Map<Class, ConfigurationInterface> getConfigurations()
    {
        return configurations;
    }

    public <T extends ContainerAwareInterface> void setConfiguration(Class<T> serviceClass, ConfigurationInterface configuration)
    {
        getConfigurations().put(serviceClass, configuration);
    }

    public <T extends ContainerAwareInterface> ConfigurationInterface getConfiguration(Class<T> serviceClass)
    {
        return getConfigurations().get(serviceClass);
    }

    public void addReferenceResolver(ReferenceResolverInterface resolver)
    {
        referenceResolvers.add(resolver);
    }
}
