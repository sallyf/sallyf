package com.sallyf.sallyf.Container.TypeResolver;

import com.sallyf.sallyf.Container.*;

public class ServiceResolver<T extends ContainerAwareInterface> implements TypeResolverInterface<T, ContainerAwareInterface>
{
    @Override
    public boolean supports(ServiceDefinition<T> serviceDefinition, Class type)
    {
        return ContainerAwareInterface.class.isAssignableFrom(type);
    }

    @Override
    public ReferenceInterface resolve(ServiceDefinition<T> serviceDefinition, Class<ContainerAwareInterface> type) throws Exception
    {
        return new ServiceReference<>(type);
    }
}
