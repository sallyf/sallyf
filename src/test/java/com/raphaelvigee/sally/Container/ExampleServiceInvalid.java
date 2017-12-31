package com.raphaelvigee.sally.Container;

public class ExampleServiceInvalid implements ContainerAwareInterface
{
    public ExampleServiceInvalid()
    {
    }

    @Override
    public Container getContainer()
    {
        return null;
    }
}
