package com.raphaelvigee.sally;

import Yuconz.Controller.AppController;

public class Kernel
{
    private Container container;

    public Kernel(Container container)
    {
        this.container = container;
    }

    public static Kernel newInstance()
    {
        Container container = new Container();

        container.add(YServer.class);
        container.add(Routing.class);

        return new Kernel(container);
    }

    public Container getContainer()
    {
        return container;
    }
}
