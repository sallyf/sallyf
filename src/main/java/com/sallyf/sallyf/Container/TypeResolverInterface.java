package com.sallyf.sallyf.Container;

public interface TypeResolverInterface<T extends ContainerAwareInterface, TYPE>
{
    boolean supports(ServiceDefinition<T> serviceDefinition, Class type);

    ReferenceInterface resolve(ServiceDefinition<T> serviceDefinition, Class<TYPE> type) throws Exception;
}
