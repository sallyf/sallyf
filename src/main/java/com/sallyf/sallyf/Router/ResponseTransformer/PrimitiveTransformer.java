package com.sallyf.sallyf.Router.ResponseTransformer;

import com.sallyf.sallyf.Router.Response;
import com.sallyf.sallyf.Router.ResponseTransformerInterface;

public class PrimitiveTransformer implements ResponseTransformerInterface<Object, Response>
{
    @Override
    public boolean supports(Object response)
    {
        return response instanceof String || response instanceof Number;
    }

    @Override
    public Response transform(Object response)
    {
        return new Response(response.toString());
    }
}
