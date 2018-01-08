package com.sallyf.sallyf.Container.TypeResolver;

import com.sallyf.sallyf.Container.*;

public class ConfigurationResolver<T extends ContainerAwareInterface> implements TypeResolverInterface<T, ConfigurationInterface>
{
    @Override
    public boolean supports(ServiceDefinition serviceDefinition, Class type)
    {
        return ConfigurationInterface.class.isAssignableFrom(type);
    }

    @Override
    public ReferenceInterface resolve(ServiceDefinition<T> serviceDefinition, Class<ConfigurationInterface> type) throws Exception
    {
        return serviceDefinition.getConfigurationReference();
    }
}
