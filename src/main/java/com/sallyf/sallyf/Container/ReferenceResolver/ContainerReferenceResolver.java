package com.sallyf.sallyf.Container.ReferenceResolver;

import com.sallyf.sallyf.Container.*;

public class ContainerReferenceResolver<T extends ServiceInterface> implements ReferenceResolverInterface<T, ContainerReference, Container>
{
    private Container container;

    public ContainerReferenceResolver(Container container)
    {
        this.container = container;
    }

    @Override
    public boolean supports(ServiceDefinition serviceDefinition, ReferenceInterface reference)
    {
        return reference instanceof ContainerReference;
    }

    @Override
    public Container resolve(ServiceDefinition<T> serviceDefinition, ContainerReference reference)
    {
        return container;
    }
}
