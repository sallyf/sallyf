package com.raphaelvigee.sally.Router.ActionParameterResolver;

import com.raphaelvigee.sally.Container.Container;
import com.raphaelvigee.sally.Router.ActionParameterResolverInterface;
import com.raphaelvigee.sally.Router.RouteParameters;
import com.raphaelvigee.sally.Router.Router;
import com.raphaelvigee.sally.Server.RuntimeBag;

public class RouteParameterResolver implements ActionParameterResolverInterface
{
    private Container container;

    public RouteParameterResolver(Container container)
    {
        this.container = container;
    }

    @Override
    public boolean supports(Class parameterType, RuntimeBag runtimeBag)
    {
        return parameterType == RouteParameters.class;
    }

    @Override
    public Object resolve(Class parameterType, RuntimeBag runtimeBag)
    {
        return container.get(Router.class).getRouteParameters(runtimeBag);
    }
}
