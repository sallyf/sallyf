package com.raphaelvigee.sally.Event;

import com.raphaelvigee.sally.EventDispatcher.EventInterface;
import com.raphaelvigee.sally.Router.RouteParameters;
import com.raphaelvigee.sally.Server.HTTPSession;

public class RouteParametersEvent implements EventInterface
{
    public HTTPSession session;

    public RouteParameters parameterValues;

    public RouteParametersEvent(HTTPSession session, RouteParameters parameterValues) {

        this.session = session;
        this.parameterValues = parameterValues;
    }
}
