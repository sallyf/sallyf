package com.raphaelvigee.sally.Authentication;

import com.raphaelvigee.sally.Container.Container;
import com.raphaelvigee.sally.Server.RuntimeBag;

@FunctionalInterface
public interface SecurityValidator
{
    boolean test(Container container, UserInterface user, RuntimeBag runtimeBag);
}
