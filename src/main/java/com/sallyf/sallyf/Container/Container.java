package com.sallyf.sallyf.Container;

import com.sallyf.sallyf.Exception.ServiceInstanciationException;
import com.sallyf.sallyf.Utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Container
{
    private Map<Class, ContainerAwareInterface> services;

    public Container()
    {
        services = new HashMap<>();
    }

    public <T extends ContainerAwareInterface> T add(Class<T> serviceClass) throws ServiceInstanciationException
    {
        return add(serviceClass, serviceClass);
    }

    public <T extends ContainerAwareInterface> T add(Class serviceAliasClass, Class<T> serviceClass) throws ServiceInstanciationException
    {
        return add(serviceAliasClass, serviceClass, null);
    }

    public <T extends ContainerAwareInterface> T add(Class<T> serviceClass, ConfigurationInterface configuration) throws ServiceInstanciationException
    {
        return add(serviceClass, serviceClass, configuration);
    }

    public <T extends ContainerAwareInterface> T add(Class serviceAliasClass, Class<T> serviceClass, ConfigurationInterface configuration) throws ServiceInstanciationException
    {
        return add(new ServiceDefinition<>(serviceAliasClass, serviceClass, configuration));
    }

    public <T extends ContainerAwareInterface> T add(ServiceDefinition<T> serviceDefinition) throws ServiceInstanciationException
    {
        Class<T> serviceClass = serviceDefinition.type;

        T instance = null;

        for (ConstructorDefinition constructorDefinition : serviceDefinition.constructorDefinitions) {
            try {
                Object[] args = resolveReferences(constructorDefinition.args);
                instance = serviceClass.getConstructor(Utils.getClasses(args)).newInstance(args);
                break;
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {
            }
        }

        if (null == instance) {
            throw new ServiceInstanciationException("No valid constructor available");
        }

        for (CallDefinition callDefinition : serviceDefinition.callDefinitions) {
            try {
                Object[] args = resolveReferences(callDefinition.args);

                Method setContainer = serviceClass.getMethod(callDefinition.name, Utils.getClasses(args));
                try {
                    setContainer.invoke(instance, args);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new ServiceInstanciationException(e);
                }
            } catch (NoSuchMethodException ignored) {
            }
        }

        if (instance.getContainer() == null) {
            throw new ServiceInstanciationException("Unable to inject Container, you need to implement a `setContainer` method or a valid constructor.");
        }

        services.put(serviceDefinition.alias, instance);

        try {
            Method initialize = serviceClass.getMethod("initialize");
            try {
                initialize.invoke(instance);
            } catch (IllegalAccessException | InvocationTargetException ignored) {
            }
        } catch (NoSuchMethodException ignored) {
        }

        return instance;
    }

    public Object[] resolveReferences(ReferenceInterface[] references) throws ServiceInstanciationException
    {
        Object[] services = new Object[references.length];

        int i = 0;
        for (ReferenceInterface reference : references) {
            services[i++] = resolveReference(reference);
        }

        return services;
    }

    public Object resolveReference(ReferenceInterface reference) throws ServiceInstanciationException
    {
        if (reference instanceof ContainerReference) {
            return this;
        }

        if (reference instanceof PlainReference) {
            return ((PlainReference) reference).value;
        }

        if (reference instanceof ServiceReference) {
            return resolveServiceReference((ServiceReference<? extends ContainerAwareInterface>) reference);
        }

        throw new ServiceInstanciationException("Unhandled reference type");
    }

    private <T extends ContainerAwareInterface> T resolveServiceReference(ServiceReference<T> serviceReference) throws ServiceInstanciationException
    {
        Class<T> type = serviceReference.type;

        if (!has(type)) {
            throw new ServiceInstanciationException("Unable to resolve " + type);
        }

        return get(type);
    }

    public <T extends ContainerAwareInterface> T get(Class<T> type)
    {
        return (T) services.get(type);
    }

    public <C extends ContainerAwareInterface, T extends C> C get(Class<T> type, Class<C> castType)
    {
        return (C) get(type);
    }

    public <T extends ContainerAwareInterface> boolean has(Class<T> type)
    {
        return services.containsKey(type);
    }
}
