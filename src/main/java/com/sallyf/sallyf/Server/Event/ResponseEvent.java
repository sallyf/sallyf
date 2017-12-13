package com.sallyf.sallyf.Server.Event;

import com.sallyf.sallyf.EventDispatcher.EventInterface;
import com.sallyf.sallyf.Router.Response;
import com.sallyf.sallyf.Server.HTTPSession;

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
