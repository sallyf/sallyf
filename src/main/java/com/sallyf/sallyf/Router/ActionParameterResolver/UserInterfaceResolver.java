package com.sallyf.sallyf.Router.ActionParameterResolver;

import com.sallyf.sallyf.Authentication.AuthenticationManager;
import com.sallyf.sallyf.Authentication.UserInterface;
import com.sallyf.sallyf.Router.ActionParameterResolverInterface;
import com.sallyf.sallyf.Server.RuntimeBag;

public class UserInterfaceResolver implements ActionParameterResolverInterface
{
    private AuthenticationManager authenticationManager;

    public UserInterfaceResolver(AuthenticationManager authenticationManager)
    {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public boolean supports(Class parameterType, RuntimeBag runtimeBag)
    {
        return UserInterface.class.isAssignableFrom(parameterType);
    }

    @Override
    public Object resolve(Class parameterType, RuntimeBag runtimeBag)
    {
        return authenticationManager.getUser(runtimeBag);
    }
}
