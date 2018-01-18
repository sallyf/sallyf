package com.sallyf.sallyf.Container.TypeResolver;

import com.sallyf.sallyf.Container.*;

public class ContainerResolver<T extends ServiceInterface> implements TypeResolverInterface<T, Container>
{
    @Override
    public boolean supports(ServiceDefinition<T> serviceDefinition, Class type)
    {
        return Container.class.isAssignableFrom(type);
    }

    @Override
    public ReferenceInterface resolve(ServiceDefinition<T> serviceDefinition, Class<Container> type)
    {
        return new ContainerReference();
    }
}
