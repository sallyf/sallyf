package com.sallyf.sallyf.Router;

import com.sallyf.sallyf.Server.RuntimeBag;

@FunctionalInterface
public interface ActionWrapperInterface
{
    Object apply(RuntimeBag runtimeBag);
}
