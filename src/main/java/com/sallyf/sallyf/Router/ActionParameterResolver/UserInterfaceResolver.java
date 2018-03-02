package com.sallyf.sallyf.Router.ActionParameterResolver;

import com.sallyf.sallyf.Authentication.AuthenticationManager;
import com.sallyf.sallyf.Authentication.UserInterface;
import com.sallyf.sallyf.Router.ActionParameterResolverInterface;
import com.sallyf.sallyf.Server.RuntimeBag;

import java.lang.reflect.Parameter;

public class UserInterfaceResolver implements ActionParameterResolverInterface
{
    private AuthenticationManager authenticationManager;

    public UserInterfaceResolver(AuthenticationManager authenticationManager)
    {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public boolean supports(Parameter parameter, RuntimeBag runtimeBag)
    {
        return UserInterface.class.isAssignableFrom(parameter.getType());
    }

    @Override
    public Object resolve(Parameter parameter, RuntimeBag runtimeBag)
    {
        return authenticationManager.getUser(runtimeBag);
    }
}
