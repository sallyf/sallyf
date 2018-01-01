package com.raphaelvigee.sally.Authentication.Validator;

import com.raphaelvigee.sally.Authentication.SecurityValidator;
import com.raphaelvigee.sally.Authentication.UserInterface;
import com.raphaelvigee.sally.Container.Container;
import com.raphaelvigee.sally.Server.RuntimeBag;

public class LoggedInValidator implements SecurityValidator
{
    @Override
    public boolean test(Container container, UserInterface user, RuntimeBag runtimeBag)
    {
        return null != user;
    }
}
