package com.sallyf.sallyf.Container.ReferenceResolver;

import com.sallyf.sallyf.Container.*;

public class ServiceReferenceResolver<T extends ServiceInterface> implements ReferenceResolverInterface<T, ServiceReference<T>, T>
{
    private Container container;

    public ServiceReferenceResolver(Container container)
    {
        this.container = container;
    }

    @Override
    public boolean supports(ServiceDefinition serviceDefinition, ReferenceInterface reference)
    {
        return reference instanceof ServiceReference;
    }

    @Override
    public T resolve(ServiceDefinition<T> serviceDefinition, ServiceReference<T> reference)
    {
        return container.get(reference.getAlias());
    }
}
