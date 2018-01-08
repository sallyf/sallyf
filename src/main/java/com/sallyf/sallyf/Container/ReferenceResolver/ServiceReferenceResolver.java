package com.sallyf.sallyf.Container.ReferenceResolver;

import com.sallyf.sallyf.Container.*;
import com.sallyf.sallyf.Container.Exception.ReferenceResolutionException;

public class ServiceReferenceResolver<T extends ContainerAwareInterface> implements ReferenceResolverInterface<T, ServiceReference<T>, T>
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
    public T resolve(ServiceDefinition<T> serviceDefinition, ServiceReference<T> reference) throws Exception
    {
        Class<T> type = reference.type;

        if (!container.has(type)) {
            throw new ReferenceResolutionException("Unable to resolve service " + type.getName());
        }

        return container.get(type);
    }
}
