package com.sallyf.sallyf.Container;

public interface ServiceRepresentationInterface<T extends ContainerAwareInterface>
{
    Class<T> getAlias();
}
