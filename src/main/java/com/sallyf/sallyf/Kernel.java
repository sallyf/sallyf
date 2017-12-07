package com.sallyf.sallyf;

public class Kernel
{
    private Container container;

    public Kernel(Container container)
    {
        this.container = container;
    }

    public static Kernel newInstance()
    {
        return new Kernel(new Container());
    }

    public Container getContainer()
    {
        return container;
    }
}
