package com.sallyf.sallyf.Router.ResponseTransformer;

import com.sallyf.sallyf.Router.Response;
import com.sallyf.sallyf.Router.ResponseTransformerInterface;
import com.sallyf.sallyf.Server.RuntimeBag;

public class PrimitiveTransformer implements ResponseTransformerInterface
{
    @Override
    public boolean supports(RuntimeBag runtimeBag, Object response)
    {
        return response instanceof String || response instanceof Number;
    }

    @Override
    public Response resolve(RuntimeBag runtimeBag, Object response)
    {
        return new Response(response.toString());
    }
}
