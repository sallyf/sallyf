package com.sallyf.sallyf.Container;

import com.sallyf.sallyf.Container.Exception.ReferenceResolutionException;

public interface ReferenceResolverInterface<T extends ContainerAwareInterface, REF extends ReferenceInterface, R>
{
    boolean supports(ServiceDefinition serviceDefinition, ReferenceInterface reference);

    R resolve(ServiceDefinition<T> serviceDefinition, REF reference) throws ReferenceResolutionException;
}
