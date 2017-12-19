package com.sallyf.sallyf.Container;

public class ExampleServiceImplements implements ContainerAwareInterface
{
    private Container container;

    public ExampleServiceImplements()
    {
    }

    public void setContainer(Container container)
    {
        this.container = container;
    }

    @Override
    public Container getContainer()
    {
        return container;
    }
}
