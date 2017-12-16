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

    public <T extends ContainerAwareInterface> T add(Class<T> type) throws ServiceInstanciationException
    {
        return add(type, new Object[]{this});
    }

    public <T extends ContainerAwareInterface> T add(Class<T> type, boolean injectContainer) throws ServiceInstanciationException
    {
        if (!injectContainer) {
            return add(type, new Object[]{});
        }

        return add(type, new Object[]{this});
    }

    public <T extends ContainerAwareInterface> T add(Class<T> className, Class<T> type) throws ServiceInstanciationException
    {
        return add(className, type, new Object[]{this});
    }

    public <T extends ContainerAwareInterface> T add(Class<T> className, Class<T> type, boolean injectContainer) throws ServiceInstanciationException
    {
        if (!injectContainer) {
            return add(className, type, new Object[]{});
        }

        return add(className, type, new Object[]{this});
    }

    public <T extends ContainerAwareInterface> T add(Class<T> type, Object[] parameters) throws ServiceInstanciationException
    {
        return add(type.toString(), type, parameters);
    }

    public <T extends ContainerAwareInterface> T add(Class<T> className, Class<T> type, Object[] parameters) throws ServiceInstanciationException
    {
        return add(className.toString(), type, parameters);
    }

    public <T extends ContainerAwareInterface> T add(String name, Class<T> type, Object[] parameters) throws ServiceInstanciationException
    {
        T instance;

        boolean parametersContainsContainer = false;

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
            instance = type.getConstructor(parameterTypes).newInstance(parameters);
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new ServiceInstanciationException(e);
        }

        if (!parametersContainsContainer) {
            try {
                Method setContainer = type.getMethod("setContainer", Container.class);
                try {
                    setContainer.invoke(instance, this);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new ServiceInstanciationException(e);
                }
            } catch (NoSuchMethodException e) {
                throw new ServiceInstanciationException("Unable to inject Container");
            }
        }

        if (instance.getContainer() != this) {
            throw new ServiceInstanciationException("Container cannot be accessed");
        }

        services.put(name, instance);

        return instance;
    }

    public <T extends ContainerAwareInterface> T get(Class<T> type)
    {
        return (T) get(type.toString());
    }

    public Object get(String name)
    {
        return services.get(name);
    }
}
