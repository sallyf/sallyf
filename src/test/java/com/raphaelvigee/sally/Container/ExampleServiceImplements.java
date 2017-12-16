package com.raphaelvigee.sally.Container;

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
