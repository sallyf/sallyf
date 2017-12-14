package com.sallyf.sallyf.Event;

import com.sallyf.sallyf.EventDispatcher.EventInterface;
import com.sallyf.sallyf.Server.HTTPSession;

public class RouteMatchEvent implements EventInterface
{
    public HTTPSession session;

    public RouteMatchEvent(HTTPSession session)
    {
        this.session = session;
    }
}
