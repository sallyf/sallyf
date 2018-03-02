package com.sallyf.sallyf;

import com.sallyf.sallyf.Router.RouteParameterResolverInterface;
import com.sallyf.sallyf.Server.RuntimeBag;

public class CapitalizerResolver implements RouteParameterResolverInterface<String>
{
    public CapitalizerResolver()
    {
    }

    @Override
    public String resolve(String name, String value, RuntimeBag runtimeBag)
    {
        return value.toUpperCase();
    }
}
