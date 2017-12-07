package com.raphaelvigee.sally;

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
