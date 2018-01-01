package com.raphaelvigee.sally.Event;

import com.raphaelvigee.sally.EventDispatcher.EventInterface;
import com.raphaelvigee.sally.Server.RuntimeBag;

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
