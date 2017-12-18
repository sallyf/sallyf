package com.sallyf.sallyf.Event;

import com.sallyf.sallyf.EventDispatcher.EventInterface;
import com.sallyf.sallyf.Router.RouteParameters;
import com.sallyf.sallyf.Server.RuntimeBag;

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
