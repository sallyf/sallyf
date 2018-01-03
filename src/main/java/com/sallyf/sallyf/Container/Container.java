package com.sallyf.sallyf.Container;

import com.sallyf.sallyf.Exception.ServiceInstantiationException;
import com.sallyf.sallyf.Utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Container
{
    private Map<Class, ContainerAwareInterface> services;

    private Map<Class, ServiceDefinition<? extends ContainerAwareInterface>> serviceDefinitions;

    private boolean instantiated = false;

    public Container()
    {
        services = new HashMap<>();
        serviceDefinitions = new HashMap<>();
    }

    public void addAll(ServiceDefinition<? extends ContainerAwareInterface>[] serviceDefinitions) throws ServiceInstantiationException
    {
        for (ServiceDefinition<? extends ContainerAwareInterface> serviceDefinition : serviceDefinitions) {
            add(serviceDefinition);
        }
    }

    public <T extends ContainerAwareInterface> void add(ServiceDefinition<T> serviceDefinition) throws ServiceInstantiationException
    {
        if(instantiated) {
            throw new ServiceInstantiationException("Container is already instantiated");
        }

        serviceDefinitions.put(serviceDefinition.alias, serviceDefinition);
    }

    public void instantiateServices() throws ServiceInstantiationException
    {
        for (Map.Entry<Class, ServiceDefinition<? extends ContainerAwareInterface>> entry : serviceDefinitions.entrySet()) {
            instantiateService(entry.getValue());
        }

        instantiated = true;
    }

    public <T extends ContainerAwareInterface> T instantiateService(ServiceDefinition<T> serviceDefinition) throws ServiceInstantiationException
    {
        Class<T> serviceClass = serviceDefinition.type;

        T instance = null;

        for (ConstructorDefinition constructorDefinition : serviceDefinition.constructorDefinitions) {
            try {
                Object[] args = resolveReferences(constructorDefinition.args);
                Constructor<?> constructor = Utils.getConstructorForArgs(serviceClass, Utils.getClasses(args));
                if (constructor != null) {
                    instance = (T) constructor.newInstance(args);
                    break;
                }
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException ignored) {
            }
        }

        if (null == instance) {
            throw new ServiceInstantiationException("No valid constructor available");
        }

        for (CallDefinition callDefinition : serviceDefinition.callDefinitions) {
            try {
                Object[] args = resolveReferences(callDefinition.args);

                Method setContainer = serviceClass.getMethod(callDefinition.name, Utils.getClasses(args));
                try {
                    setContainer.invoke(instance, args);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new ServiceInstantiationException(e);
                }
            } catch (NoSuchMethodException ignored) {
            }
        }

        if (instance.getContainer() == null) {
            throw new ServiceInstantiationException("Unable to inject Container, you need to implement a `setContainer` method or a valid constructor.");
        }

        services.put(serviceDefinition.alias, instance);

        try {
            instance.initialize();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceInstantiationException(e);
        }

        return instance;
    }

    public Object[] resolveReferences(ReferenceInterface[] references) throws ServiceInstantiationException
    {
        Object[] services = new Object[references.length];

        int i = 0;
        for (ReferenceInterface reference : references) {
            services[i++] = resolveReference(reference);
        }

        return services;
    }

    public Object resolveReference(ReferenceInterface reference) throws ServiceInstantiationException
    {
        if (reference instanceof ContainerReference) {
            return this;
        }

        if (reference instanceof PlainReference) {
            return ((PlainReference) reference).value;
        }

        if (reference instanceof DefaultConfigurationReference) {
            return resolveDefaultConfiguration((DefaultConfigurationReference<? extends ContainerAwareInterface>) reference);
        }

        if (reference instanceof ServiceReference) {
            return resolveServiceReference((ServiceReference<? extends ContainerAwareInterface>) reference);
        }

        throw new ServiceInstantiationException("Unhandled reference type");
    }

    private <T extends ContainerAwareInterface> T resolveServiceReference(ServiceReference<T> serviceReference) throws ServiceInstantiationException
    {
        Class<T> type = serviceReference.type;

        if (!has(type)) {
            throw new ServiceInstantiationException("Unable to resolve service " + type);
        }

        return get(type);
    }

    private <T extends ContainerAwareInterface> ConfigurationInterface resolveDefaultConfiguration(DefaultConfigurationReference<T> configurationReference) throws ServiceInstantiationException
    {
        Class<T> type = configurationReference.serviceReference.type;

        try {
            Method method = type.getDeclaredMethod("getDefaultConfigurationClass");
            Class<? extends ConfigurationInterface> configurationClass = (Class) method.invoke(null);

            if (configurationClass == null) {
                return null;
            }

            return configurationClass.newInstance();
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new ServiceInstantiationException(e);
        } catch (NoSuchMethodException ignored) {
        }

        return null;
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
}
