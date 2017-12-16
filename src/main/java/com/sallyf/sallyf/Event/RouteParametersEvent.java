package com.sallyf.sallyf.Event;

import com.sallyf.sallyf.EventDispatcher.EventInterface;
import com.sallyf.sallyf.Router.RouteParameters;
import com.sallyf.sallyf.Server.Request;

public class RouteParametersEvent implements EventInterface
{
    public Request request;

    public RouteParameters parameterValues;

    public RouteParametersEvent(Request request, RouteParameters parameterValues) {

        this.request = request;
        this.parameterValues = parameterValues;
    }
}
