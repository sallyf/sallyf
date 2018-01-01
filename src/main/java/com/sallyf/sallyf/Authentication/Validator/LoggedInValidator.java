package com.sallyf.sallyf.Authentication.Validator;

import com.sallyf.sallyf.Authentication.SecurityValidator;
import com.sallyf.sallyf.Authentication.UserInterface;
import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Server.RuntimeBag;

public class LoggedInValidator implements SecurityValidator
{
    @Override
    public boolean test(Container container, UserInterface user, RuntimeBag runtimeBag)
    {
        return null != user;
    }
}
