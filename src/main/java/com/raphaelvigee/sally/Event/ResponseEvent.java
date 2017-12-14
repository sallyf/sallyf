package com.raphaelvigee.sally.Event;

import com.raphaelvigee.sally.EventDispatcher.EventInterface;
import com.raphaelvigee.sally.Router.Response;
import com.raphaelvigee.sally.Server.HTTPSession;

public class ResponseEvent implements EventInterface
{
    public HTTPSession session;

    public Response response;

    public ResponseEvent(HTTPSession session, Response response)
    {
        this.session = session;
        this.response = response;
    }
}
