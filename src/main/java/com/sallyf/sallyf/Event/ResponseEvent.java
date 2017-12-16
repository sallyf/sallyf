package com.sallyf.sallyf.Event;

import com.sallyf.sallyf.EventDispatcher.EventInterface;
import com.sallyf.sallyf.Router.Response;
import com.sallyf.sallyf.Server.Request;

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
