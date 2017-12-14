package com.raphaelvigee.sally.Event;

import com.raphaelvigee.sally.EventDispatcher.EventInterface;
import com.raphaelvigee.sally.Server.HTTPSession;

public class RouteMatchEvent implements EventInterface
{
    public HTTPSession session;

    public RouteMatchEvent(HTTPSession session)
    {
        this.session = session;
    }
}
