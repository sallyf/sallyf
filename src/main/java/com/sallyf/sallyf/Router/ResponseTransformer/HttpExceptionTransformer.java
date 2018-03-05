package com.sallyf.sallyf.Router.ResponseTransformer;

import com.sallyf.sallyf.Exception.HttpException;
import com.sallyf.sallyf.Router.Response;
import com.sallyf.sallyf.Router.ResponseTransformerInterface;
import com.sallyf.sallyf.Server.Status;

public class HttpExceptionTransformer implements ResponseTransformerInterface<HttpException, Response>
{
    @Override
    public boolean supports(Object response)
    {
        return response instanceof HttpException;
    }

    @Override
    public Response transform(HttpException response)
    {
        Status status = response.getStatus();

        return new Response(status.getDescription(), status, "text/html");
    }
}
