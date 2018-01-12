package com.sallyf.sallyf.Container;

public class ServiceWithCall implements ContainerAwareInterface
{
    public ServiceWithCall()
    {
    }

    Container container;

    public Container getContainer()
    {
        return container;
    }

    public void setContainer(Container container)
    {
        this.container = container;
    }
}
