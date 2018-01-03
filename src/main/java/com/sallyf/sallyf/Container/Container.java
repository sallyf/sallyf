package com.sallyf.sallyf.Container;

import com.sallyf.sallyf.Exception.ServiceInstanciationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Container
{
    private Map<Class<? extends ContainerAwareInterface>, ContainerAwareInterface> services;

    public Container()
    {
        services = new HashMap<>();
    }

    public <T extends ContainerAwareInterface> T add(Class<T> serviceClass) throws ServiceInstanciationException
    {
        return add(serviceClass, new Object[]{this});
    }

    public <N extends ContainerAwareInterface, T extends N> T add(Class<N> serviceReferenceClass, Class<T> serviceClass) throws ServiceInstanciationException
    {
        return add(serviceReferenceClass, serviceClass, new Object[]{this});
    }

    public <T extends ContainerAwareInterface> T add(Class<T> serviceClass, Object[] parameters) throws ServiceInstanciationException
    {
        return add(serviceClass, serviceClass, parameters);
    }

    public <N extends ContainerAwareInterface, T extends N> T add(Class<N> serviceReferenceClass, Class<T> serviceClass, Object[] parameters) throws ServiceInstanciationException
    {
        T instance;

        Class[] parameterTypes = new Class[parameters.length];
        int i = 0;
        for (Object parameter : parameters) {
            Class parameterClass = parameter.getClass();
            if (parameterClass == this.getClass()) {
            }
            parameterTypes[i++] = parameterClass;
        }

        try {
            instance = serviceClass.getConstructor(parameterTypes).newInstance(parameters);
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            try {
                instance = serviceClass.getConstructor().newInstance();
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e2) {
                e.addSuppressed(e2);
                throw new ServiceInstanciationException(e);
            }
        }

        if (instance.getContainer() == null) {
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

        services.put(serviceReferenceClass, instance);

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
