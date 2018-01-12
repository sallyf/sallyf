package com.sallyf.sallyf.Container;

public interface ServiceAliasAwareInterface<T extends ContainerAwareInterface>
{
    Class<T> getAlias();
}
