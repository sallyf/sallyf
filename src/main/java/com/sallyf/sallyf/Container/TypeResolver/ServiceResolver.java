package com.sallyf.sallyf.Container.TypeResolver;

import com.sallyf.sallyf.Container.*;

public class ServiceResolver<T extends ServiceInterface> implements TypeResolverInterface<T, ServiceInterface>
{
    @Override
    public boolean supports(ServiceDefinition<T> serviceDefinition, Class type)
    {
        return ServiceInterface.class.isAssignableFrom(type);
    }

    @Override
    public ReferenceInterface resolve(ServiceDefinition<T> serviceDefinition, Class<ServiceInterface> type)
    {
        return new ServiceReference<>(type);
    }
}
