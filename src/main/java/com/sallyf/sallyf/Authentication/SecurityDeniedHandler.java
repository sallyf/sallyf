package com.sallyf.sallyf.Authentication;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Server.RuntimeBag;

@FunctionalInterface
public interface SecurityDeniedHandler
{
    Object apply(Container container, RuntimeBag runtimeBag);
}
