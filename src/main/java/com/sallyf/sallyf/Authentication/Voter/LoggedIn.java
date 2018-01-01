package com.sallyf.sallyf.Authentication.Voter;

import com.sallyf.sallyf.Authentication.UserInterface;
import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Server.RuntimeBag;

public class LoggedIn implements VoterInterface
{
    @Override
    public boolean test(Container container, UserInterface user, RuntimeBag runtimeBag)
    {
        return null != user;
    }
}
