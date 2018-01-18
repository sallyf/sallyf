package com.sallyf.sallyf.Container;

public class ServiceWithCall implements ServiceInterface
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
