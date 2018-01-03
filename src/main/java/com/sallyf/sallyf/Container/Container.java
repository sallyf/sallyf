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

        if (reference instanceof DefaultConfigurationReference) {
            return resolveDefaultConfiguration((DefaultConfigurationReference<? extends ContainerAwareInterface>) reference);
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

    private <T extends ContainerAwareInterface> ConfigurationInterface resolveDefaultConfiguration(DefaultConfigurationReference<T> configurationReference) throws ServiceInstanciationException
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
            throw new ServiceInstanciationException(e);
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
