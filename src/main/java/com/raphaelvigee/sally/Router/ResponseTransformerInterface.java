package com.raphaelvigee.sally.Router;

import com.raphaelvigee.sally.Server.RuntimeBag;

public interface ResponseTransformerInterface<I, R>
{
    boolean supports(RuntimeBag runtimeBag, Object response);

    R transform(RuntimeBag runtimeBag, I response);
}
