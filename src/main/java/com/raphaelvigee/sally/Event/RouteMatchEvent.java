package com.raphaelvigee.sally.Event;

import com.raphaelvigee.sally.EventDispatcher.EventInterface;
import com.raphaelvigee.sally.Router.Route;
import org.eclipse.jetty.server.Request;

public class RouteMatchEvent implements EventInterface
{
    public Request request;

    private Route route;

    public RouteMatchEvent(Request request, Route route)
    {
        this.request = request;
        this.route = route;
    }
}
