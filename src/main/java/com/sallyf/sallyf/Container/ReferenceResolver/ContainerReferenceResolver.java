package com.sallyf.sallyf.Container.ReferenceResolver;

import com.sallyf.sallyf.Container.*;
import com.sallyf.sallyf.Container.Exception.ReferenceResolutionException;

public class ContainerReferenceResolver<T extends ContainerAwareInterface> implements ReferenceResolverInterface<T, ContainerReference, Container>
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
    public Container resolve(ServiceDefinition<T> serviceDefinition, ContainerReference reference) throws ReferenceResolutionException
    {
        return container;
    }
}