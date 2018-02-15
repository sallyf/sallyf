package com.sallyf.sallyf.Container.ReferenceResolver;

import com.sallyf.sallyf.Container.*;
import com.sallyf.sallyf.Exception.FrameworkException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ConfigurationReferenceResolver<T extends ServiceInterface> implements ReferenceResolverInterface<T, DefaultConfigurationReference, ConfigurationInterface>
{
    private Container container;

    public ConfigurationReferenceResolver(Container container)
    {
        this.container = container;
    }

    @Override
    public boolean supports(ServiceDefinition serviceDefinition, ReferenceInterface reference)
    {
        return reference instanceof DefaultConfigurationReference;
    }

    @Override
    public ConfigurationInterface resolve(ServiceDefinition<T> serviceDefinition, DefaultConfigurationReference reference)
    {
        Class<T> type = serviceDefinition.getType();

        try {
            Method method = type.getDeclaredMethod("getDefaultConfigurationClass");
            Class<? extends ConfigurationInterface> defaultConfigurationClass = (Class) method.invoke(null);

            if (defaultConfigurationClass == null) {
                return null;
            }

            return defaultConfigurationClass.newInstance();
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new FrameworkException(e);
        } catch (NoSuchMethodException ignored) {
        }

        return null;
    }
}
