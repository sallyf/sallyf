package com.sallyf.sallyf.Authentication;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Server.RuntimeBag;

@FunctionalInterface
public interface SecurityValidator
{
    boolean test(Container container, UserInterface user, RuntimeBag runtimeBag);
}
