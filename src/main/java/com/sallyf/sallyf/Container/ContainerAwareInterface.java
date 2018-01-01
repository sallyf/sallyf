package com.sallyf.sallyf.Container;


public interface ContainerAwareInterface
{
    Container getContainer();

    default void initialize() {}
}
