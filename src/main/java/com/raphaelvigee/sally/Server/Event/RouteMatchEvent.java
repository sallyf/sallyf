package com.raphaelvigee.sally.Server.Event;

import com.raphaelvigee.sally.EventDispatcher.EventInterface;
import com.raphaelvigee.sally.Router.Route;
import com.raphaelvigee.sally.Server.HTTPSession;

public class RouteMatchEvent implements EventInterface
{
    public HTTPSession session;

    public Route match;

    public RouteMatchEvent(HTTPSession session, Route match)
    {
        this.session = session;
        this.match = match;
    }
}
