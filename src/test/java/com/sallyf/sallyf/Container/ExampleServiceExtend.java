package com.sallyf.sallyf.Container;

public class ExampleServiceExtend implements ContainerAwareInterface
{
    private Container container;

    public ExampleServiceExtend(Container container)
    {
        this.container = container;
    }
}
