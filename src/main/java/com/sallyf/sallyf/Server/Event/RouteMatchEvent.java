package com.sallyf.sallyf.Server.Event;

import com.sallyf.sallyf.EventDispatcher.EventInterface;
import com.sallyf.sallyf.Router.Route;
import com.sallyf.sallyf.Server.HTTPSession;

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
