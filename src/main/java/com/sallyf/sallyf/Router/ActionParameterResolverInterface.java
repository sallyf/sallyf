package com.sallyf.sallyf.Router;

import com.sallyf.sallyf.Server.RuntimeBag;

import java.lang.reflect.Parameter;

public interface ActionParameterResolverInterface<R>
{
    boolean supports(Parameter parameter, RuntimeBag runtimeBag);

    R resolve(Parameter parameter, RuntimeBag runtimeBag);
}
