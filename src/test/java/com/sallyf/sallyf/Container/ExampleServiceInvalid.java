package com.sallyf.sallyf.Container;

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
