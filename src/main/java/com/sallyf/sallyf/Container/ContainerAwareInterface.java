package com.sallyf.sallyf.Container;


public interface ContainerAwareInterface
{
    Container container = null;

    default Container getContainer()
    {
        return container;
    }

    default void setContainer(Container c)
    {
        System.err.println("Unimplemented setContainer");
    }
}
