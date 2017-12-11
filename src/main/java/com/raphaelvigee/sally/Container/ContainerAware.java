package com.raphaelvigee.sally.Container;

abstract public class ContainerAware implements ContainerAwareInterface
{
    private Container container;

    public ContainerAware()
    {
    }

    public void setContainer(Container container)
    {
        this.container = container;
    }
}
