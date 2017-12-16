package com.raphaelvigee.sally.Event;

import com.raphaelvigee.sally.EventDispatcher.EventInterface;
import com.raphaelvigee.sally.Server.Request;

public class RequestEvent implements EventInterface
{
    public Request session;

    public RequestEvent(Request session) {

        this.session = session;
    }
}
