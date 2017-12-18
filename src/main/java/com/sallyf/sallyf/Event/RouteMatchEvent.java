package com.sallyf.sallyf.Event;

import com.sallyf.sallyf.EventDispatcher.EventInterface;
import com.sallyf.sallyf.Router.Route;
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
