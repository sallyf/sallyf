package com.sallyf.sallyf.Container;

public interface ReferenceResolverInterface<T extends ServiceInterface, REF extends ReferenceInterface, R>
{
    boolean supports(ServiceDefinition serviceDefinition, ReferenceInterface reference);

    R resolve(ServiceDefinition<T> serviceDefinition, REF reference);
}
