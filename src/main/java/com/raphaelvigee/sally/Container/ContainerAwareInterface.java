package com.raphaelvigee.sally.Container;


public interface ContainerAwareInterface
{
    Container getContainer();

    default void initialize() {}
}
