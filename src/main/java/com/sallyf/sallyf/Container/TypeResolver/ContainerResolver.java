package com.sallyf.sallyf.Container.TypeResolver;

import com.sallyf.sallyf.Container.*;
import com.sallyf.sallyf.Container.TypeResolverInterface;

public class ContainerResolver<T extends ContainerAwareInterface> implements TypeResolverInterface<T, Container>
{
    @Override
    public boolean supports(ServiceDefinition<T> serviceDefinition, Class type)
    {
        return Container.class.isAssignableFrom(type);
    }

    @Override
    public ReferenceInterface resolve(ServiceDefinition<T> serviceDefinition, Class<Container> type) throws Exception
    {
        return new ContainerReference();
    }
}
