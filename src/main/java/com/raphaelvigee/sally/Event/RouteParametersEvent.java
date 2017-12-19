package com.raphaelvigee.sally.Event;

import com.raphaelvigee.sally.EventDispatcher.EventInterface;
import com.raphaelvigee.sally.Router.RouteParameters;
import com.raphaelvigee.sally.Server.RuntimeBag;

public class RouteParametersEvent implements EventInterface
{
    private RouteParameters parameterValues;

    private RuntimeBag runtimeBag;

    public RouteParametersEvent(RuntimeBag runtimeBag, RouteParameters parameterValues)
    {
        this.runtimeBag = runtimeBag;
        this.parameterValues = parameterValues;
    }
}
