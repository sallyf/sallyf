package com.raphaelvigee.sally.Event;

import com.raphaelvigee.sally.EventDispatcher.EventInterface;
import com.raphaelvigee.sally.Server.Request;

public class RouteMatchEvent implements EventInterface
{
    public Request request;

    public RouteMatchEvent(Request request)
    {
        this.request = request;
    }
}
