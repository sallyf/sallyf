package com.sallyf.sallyf.Router.ActionParameterResolver;

import com.sallyf.sallyf.Authentication.AuthenticationManager;
import com.sallyf.sallyf.Authentication.UserInterface;
import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Router.ActionParameterResolverInterface;
import com.sallyf.sallyf.Server.RuntimeBag;

public class UserInterfaceResolver implements ActionParameterResolverInterface
{
    private Container container;

    public UserInterfaceResolver(Container container)
    {
        this.container = container;
    }

    @Override
    public boolean supports(Class parameterType, RuntimeBag runtimeBag)
    {
        return UserInterface.class.isAssignableFrom(parameterType) && container.has(AuthenticationManager.class);
    }

    @Override
    public Object resolve(Class parameterType, RuntimeBag runtimeBag)
    {
        AuthenticationManager authenticationManager = container.get(AuthenticationManager.class);

        return authenticationManager.getUser(runtimeBag);
    }
}
