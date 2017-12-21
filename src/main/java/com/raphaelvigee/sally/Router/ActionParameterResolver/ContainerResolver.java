package com.raphaelvigee.sally.Router.ActionParameterResolver;

import com.raphaelvigee.sally.Container.Container;
import com.raphaelvigee.sally.Router.ActionParameterResolverInterface;
import com.raphaelvigee.sally.Server.RuntimeBag;

public class ContainerResolver implements ActionParameterResolverInterface
{
    private Container container;

    public ContainerResolver(Container container)
    {
        this.container = container;
    }

    @Override
    public boolean supports(Class parameterType, RuntimeBag runtimeBag)
    {
        return parameterType == Container.class;
    }

    @Override
    public Object resolve(Class parameterType, RuntimeBag runtimeBag)
    {
        return container;
    }
}
