package com.sallyf.sallyf.Router.ActionParameterResolver;

import com.sallyf.sallyf.Authentication.AuthenticationManager;
import com.sallyf.sallyf.Authentication.UserInterface;
import com.sallyf.sallyf.Router.ActionParameterResolverInterface;

import java.lang.reflect.Parameter;

public class UserInterfaceResolver implements ActionParameterResolverInterface
{
    private AuthenticationManager authenticationManager;

    public UserInterfaceResolver(AuthenticationManager authenticationManager)
    {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public boolean supports(Parameter parameter)
    {
        return UserInterface.class.isAssignableFrom(parameter.getType());
    }

    @Override
    public Object resolve(Parameter parameter)
    {
        return authenticationManager.getUser();
    }
}
