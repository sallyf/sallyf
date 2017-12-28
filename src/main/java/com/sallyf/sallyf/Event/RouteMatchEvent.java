package com.sallyf.sallyf.Event;

import com.sallyf.sallyf.EventDispatcher.EventInterface;
import com.sallyf.sallyf.Server.RuntimeBag;

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
