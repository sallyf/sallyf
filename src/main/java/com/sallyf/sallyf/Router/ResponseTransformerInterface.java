package com.sallyf.sallyf.Router;

import com.sallyf.sallyf.Server.RuntimeBag;

public interface ResponseTransformerInterface
{
    boolean supports(RuntimeBag runtimeBag, Object response);

    Response resolve(RuntimeBag runtimeBag, Object response);
}
