package com.sallyf.sallyf.Event;

import com.sallyf.sallyf.EventDispatcher.EventInterface;
import org.eclipse.jetty.server.Request;

public class RequestEvent implements EventInterface
{
    public Request request;

    public RequestEvent(Request request)
    {
        this.request = request;
    }
}
