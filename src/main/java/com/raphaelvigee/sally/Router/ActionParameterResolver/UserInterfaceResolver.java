package com.raphaelvigee.sally.Router.ActionParameterResolver;

import com.raphaelvigee.sally.Authentication.AuthenticationManager;
import com.raphaelvigee.sally.Authentication.UserInterface;
import com.raphaelvigee.sally.Container.Container;
import com.raphaelvigee.sally.Router.ActionParameterResolverInterface;
import com.raphaelvigee.sally.Server.RuntimeBag;

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
