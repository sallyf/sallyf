package com.raphaelvigee.sally.Authentication.Voter;

import com.raphaelvigee.sally.Authentication.UserInterface;
import com.raphaelvigee.sally.Container.Container;
import com.raphaelvigee.sally.Server.RuntimeBag;

@FunctionalInterface
public interface VoterInterface
{
    boolean test(Container container, UserInterface user, RuntimeBag runtimeBag);
}
