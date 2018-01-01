package com.raphaelvigee.sally.Router.ResponseTransformer;

import com.raphaelvigee.sally.Exception.HttpException;
import com.raphaelvigee.sally.Router.Response;
import com.raphaelvigee.sally.Router.ResponseTransformerInterface;
import com.raphaelvigee.sally.Server.RuntimeBag;
import com.raphaelvigee.sally.Server.Status;

public class HttpExceptionTransformer implements ResponseTransformerInterface<HttpException, Response>
{
    @Override
    public boolean supports(RuntimeBag runtimeBag, Object response)
    {
        return response instanceof HttpException;
    }

    @Override
    public Response transform(RuntimeBag runtimeBag, HttpException response)
    {
        Status status = response.getStatus();

        return new Response(status.getDescription(), status, "text/html");
    }
}
