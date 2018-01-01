package com.raphaelvigee.sally.Server;

import com.raphaelvigee.sally.Router.Route;
import org.eclipse.jetty.server.Request;

public class RuntimeBag
{
    private Request request;
    private Route route;

    public RuntimeBag()
    {
    }

    public RuntimeBag(Request request, Route route)
    {
        this.request = request;
        this.route = route;
    }

    public Request getRequest()
    {
        return request;
    }

    public void setRequest(Request request)
    {
        this.request = request;
    }

    public Route getRoute()
    {
        return route;
    }

    public void setRoute(Route route)
    {
        this.route = route;
    }
}
