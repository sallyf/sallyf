package com.sallyf.sallyf.Event;

import com.sallyf.sallyf.EventDispatcher.EventInterface;
import com.sallyf.sallyf.Server.Request;

public class RequestEvent implements EventInterface
{
    public Request session;

    public RequestEvent(Request session) {

        this.session = session;
    }
}
