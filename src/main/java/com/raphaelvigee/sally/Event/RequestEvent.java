package com.raphaelvigee.sally.Event;

import com.raphaelvigee.sally.EventDispatcher.EventInterface;
import org.eclipse.jetty.server.Request;

public class RequestEvent implements EventInterface
{
    public Request request;

    public RequestEvent(Request request)
    {
        this.request = request;
    }
}
