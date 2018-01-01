package com.sallyf.sallyf.Event;

import com.sallyf.sallyf.EventDispatcher.EventInterface;
import com.sallyf.sallyf.Server.RuntimeBag;

public class RouteMatchEvent implements EventInterface
{
    private RuntimeBag runtimeBag;

    public RouteMatchEvent(RuntimeBag runtimeBag)
    {
        this.runtimeBag = runtimeBag;
    }

    public RuntimeBag getRuntimeBag()
    {
        return runtimeBag;
    }

    public void setRuntimeBag(RuntimeBag runtimeBag)
    {
        this.runtimeBag = runtimeBag;
    }
}
