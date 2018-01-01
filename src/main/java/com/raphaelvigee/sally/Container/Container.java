package com.raphaelvigee.sally.Container;

import com.raphaelvigee.sally.Exception.ServiceInstanciationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Container
{
    private Map<String, ContainerAwareInterface> services;

    public Container()
    {
        services = new HashMap<>();
    }

    public <T extends ContainerAwareInterface> T add(Class<T> serviceClass) throws ServiceInstanciationException
    {
        return add(serviceClass, new Object[]{this});
    }

    public <N extends ContainerAwareInterface, T extends N> T add(Class<N> serviceNameClass, Class<T> serviceClass) throws ServiceInstanciationException
    {
        return add(serviceNameClass, serviceClass, new Object[]{this});
    }

    public <T extends ContainerAwareInterface> T add(Class<T> serviceClass, Object[] parameters) throws ServiceInstanciationException
    {
        return add(serviceClass.toString(), serviceClass, parameters);
    }

    public <N extends ContainerAwareInterface, T extends N> T add(Class<N> serviceNameClass, Class<T> serviceClass, Object[] parameters) throws ServiceInstanciationException
    {
        return add(serviceNameClass.toString(), serviceClass, parameters);
    }

    public <T extends ContainerAwareInterface> T add(String name, Class<T> serviceClass, Object[] parameters) throws ServiceInstanciationException
    {
        T instance;

        boolean parametersContainsContainer = false;
        boolean instantiatedWithContainer = false;

        Class[] parameterTypes = new Class[parameters.length];
        int i = 0;
        for (Object parameter : parameters) {
            Class parameterClass = parameter.getClass();
            if (parameterClass == this.getClass()) {
                parametersContainsContainer = true;
            }
            parameterTypes[i++] = parameterClass;
        }

        try {
            instance = serviceClass.getConstructor(parameterTypes).newInstance(parameters);
            instantiatedWithContainer = parametersContainsContainer;
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            try {
                instance = serviceClass.getConstructor().newInstance();
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e2) {
                e.addSuppressed(e2);
                throw new ServiceInstanciationException(e);
            }
        }

        if (!instantiatedWithContainer) {
            try {
                Method setContainer = serviceClass.getMethod("setContainer", Container.class);
                try {
                    setContainer.invoke(instance, this);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new ServiceInstanciationException(e);
                }
            } catch (NoSuchMethodException e) {
                throw new ServiceInstanciationException("Unable to inject Container, you need to implement a `setContainer` method.");
            }
        }

        if (instance.getContainer() != this) {
            throw new ServiceInstanciationException("Container cannot be accessed");
        }

        services.put(name, instance);

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

    public <T extends ContainerAwareInterface> T get(Class<T> type)
    {
        return (T) get(type.toString());
    }

    public <N extends ContainerAwareInterface, T extends N> T get(Class<N> type, Class<T> referenceType)
    {
        return (T) get(type.toString());
    }

    public Object get(String name)
    {
        return services.get(name);
    }

    public <T extends ContainerAwareInterface> boolean has(Class<T> type)
    {
        return has(type.toString());
    }

    public boolean has(String name)
    {
        return services.containsKey(name);
    }
}
