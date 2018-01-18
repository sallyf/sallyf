package com.sallyf.sallyf.Container;

public interface ServiceRepresentationInterface<T extends ServiceInterface>
{
    Class<T> getAlias();
}
