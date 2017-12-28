package com.sallyf.sallyf.Controller;

import com.sallyf.sallyf.Container.Container;

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
