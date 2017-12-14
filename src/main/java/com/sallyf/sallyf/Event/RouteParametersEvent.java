package com.sallyf.sallyf.Event;

import com.sallyf.sallyf.EventDispatcher.EventInterface;
import com.sallyf.sallyf.Router.RouteParameters;
import com.sallyf.sallyf.Server.HTTPSession;

public class RouteParametersEvent implements EventInterface
{
    public HTTPSession session;

    public RouteParameters parameterValues;

    public RouteParametersEvent(HTTPSession session, RouteParameters parameterValues) {

        this.session = session;
        this.parameterValues = parameterValues;
    }
}
