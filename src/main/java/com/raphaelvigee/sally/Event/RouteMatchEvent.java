package com.raphaelvigee.sally.Event;

import com.raphaelvigee.sally.EventDispatcher.EventInterface;
import com.raphaelvigee.sally.Server.RuntimeBag;

public class RouteMatchEvent implements EventInterface
{
    private RuntimeBag request;

    public RouteMatchEvent(RuntimeBag request)
    {
        this.request = request;
    }

    public RuntimeBag getRequest()
    {
        return request;
    }

    public void setRequest(RuntimeBag request)
    {
        this.request = request;
    }
}
