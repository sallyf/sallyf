package com.sallyf.sallyf.Container.ReferenceResolver;

import com.sallyf.sallyf.Container.*;
import com.sallyf.sallyf.Exception.FrameworkException;
import com.sallyf.sallyf.Utils.ClassUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ConfigurationReferenceResolver<T extends ServiceInterface> implements ReferenceResolverInterface<T, ConfigurationReference, ConfigurationInterface>
{
    private Container container;

    public ConfigurationReferenceResolver(Container container)
    {
        this.container = container;
    }

    @Override
    public boolean supports(ServiceDefinition serviceDefinition, ReferenceInterface reference)
    {
        return reference instanceof ConfigurationReference;
    }

    @Override
    public ConfigurationInterface resolve(ServiceDefinition<T> serviceDefinition, ConfigurationReference reference)
    {
        Class<T> type = serviceDefinition.getType();

        ConfigurationInterface configuration = container.getConfigurations().get(type);

        if (null != configuration) {
            return configuration;
        }

        try {
            Method method = type.getDeclaredMethod("getDefaultConfigurationClass");
            Class<? extends ConfigurationInterface> defaultConfigurationClass = (Class) method.invoke(null);

            if (defaultConfigurationClass == null) {
                return null;
            }

            return ClassUtils.newInstance(defaultConfigurationClass);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new FrameworkException(e);
        } catch (NoSuchMethodException ignored) {
        }

        return null;
    }
}
