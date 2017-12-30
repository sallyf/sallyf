package com.raphaelvigee.sally.Router;

import com.raphaelvigee.sally.Server.RuntimeBag;

public interface ResponseTransformerInterface
{
    boolean supports(RuntimeBag runtimeBag, Object response);

    Response transform(RuntimeBag runtimeBag, Object response);
}
