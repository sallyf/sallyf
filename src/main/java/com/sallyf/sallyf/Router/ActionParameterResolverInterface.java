package com.sallyf.sallyf.Router;

import com.sallyf.sallyf.Server.RuntimeBag;

public interface ActionParameterResolverInterface<R>
{
    boolean supports(Class parameterType, RuntimeBag runtimeBag);

    R resolve(Class parameterType, RuntimeBag runtimeBag);
}
