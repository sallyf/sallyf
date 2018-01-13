package com.sallyf.sallyf.Router;

import com.sallyf.sallyf.Server.RuntimeBag;

public interface ResponseTransformerInterface<I, R>
{
    boolean supports(RuntimeBag runtimeBag, Object response);

    R transform(RuntimeBag runtimeBag, I response) ;
}
