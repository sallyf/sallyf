package com.sallyf.sallyf.Event;

import com.sallyf.sallyf.EventDispatcher.EventInterface;
import com.sallyf.sallyf.Server.Request;

public class RouteMatchEvent implements EventInterface
{
    public Request request;

    public RouteMatchEvent(Request request)
    {
        this.request = request;
    }
}
