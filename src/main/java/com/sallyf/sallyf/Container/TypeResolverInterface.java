package com.sallyf.sallyf.Container;

public interface TypeResolverInterface<T extends ServiceInterface, TYPE>
{
    boolean supports(ServiceDefinition<T> serviceDefinition, Class type);

    ReferenceInterface resolve(ServiceDefinition<T> serviceDefinition, Class<TYPE> type);
}
