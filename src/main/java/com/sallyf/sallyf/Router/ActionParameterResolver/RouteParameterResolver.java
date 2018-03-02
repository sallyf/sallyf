package com.sallyf.sallyf.Router.ActionParameterResolver;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Router.ActionParameterResolverInterface;
import com.sallyf.sallyf.Router.RouteParameters;
import com.sallyf.sallyf.Router.Router;
import com.sallyf.sallyf.Server.RuntimeBag;

import java.lang.reflect.Parameter;

public class RouteParameterResolver implements ActionParameterResolverInterface
{
    private Container container;

    public RouteParameterResolver(Container container)
    {
        this.container = container;
    }

    @Override
    public boolean supports(Parameter parameter, RuntimeBag runtimeBag)
    {
        return parameter.getType() == RouteParameters.class;
    }

    @Override
    public Object resolve(Parameter parameter, RuntimeBag runtimeBag)
    {
        return container.get(Router.class).getRouteParameters(runtimeBag);
    }
}
