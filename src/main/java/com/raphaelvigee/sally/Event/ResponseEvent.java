package com.raphaelvigee.sally.Event;

import com.raphaelvigee.sally.EventDispatcher.EventInterface;
import com.raphaelvigee.sally.Router.Response;
import com.raphaelvigee.sally.Server.Request;

public class ResponseEvent implements EventInterface
{
    public Request request;

    public Response response;

    public ResponseEvent(Request request, Response response)
    {
        this.request = request;
        this.response = response;
    }
}
