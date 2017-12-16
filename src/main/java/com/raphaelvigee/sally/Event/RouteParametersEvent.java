package com.raphaelvigee.sally.Event;

import com.raphaelvigee.sally.EventDispatcher.EventInterface;
import com.raphaelvigee.sally.Router.RouteParameters;
import com.raphaelvigee.sally.Server.Request;

public class RouteParametersEvent implements EventInterface
{
    public Request request;

    public RouteParameters parameterValues;

    public RouteParametersEvent(Request request, RouteParameters parameterValues) {

        this.request = request;
        this.parameterValues = parameterValues;
    }
}
