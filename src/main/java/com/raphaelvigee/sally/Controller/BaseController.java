package com.raphaelvigee.sally.Controller;

import com.raphaelvigee.sally.Container.Container;

abstract public class BaseController implements ControllerInterface
{
    private Container container;

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
