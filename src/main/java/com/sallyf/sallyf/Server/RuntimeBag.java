package com.sallyf.sallyf.Server;

import com.sallyf.sallyf.Router.Route;
import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpSession;

public class RuntimeBag
{
    private Request request;

    private Route route;

    private RuntimeStorage storage;

    public RuntimeBag()
    {
        this.storage = new RuntimeStorage();
    }

    public RuntimeBag(Request request, Route route)
    {
        this();

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

    public RuntimeStorage getStorage()
    {
        return storage;
    }
}
