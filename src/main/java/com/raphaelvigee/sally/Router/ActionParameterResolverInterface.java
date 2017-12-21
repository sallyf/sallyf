package com.raphaelvigee.sally.Router;

import com.raphaelvigee.sally.Server.RuntimeBag;

public interface ActionParameterResolverInterface<R>
{
    boolean supports(Class parameterType, RuntimeBag runtimeBag);

    R resolve(Class parameterType, RuntimeBag runtimeBag);
}
