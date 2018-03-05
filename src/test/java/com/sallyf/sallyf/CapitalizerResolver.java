package com.sallyf.sallyf;

import com.sallyf.sallyf.Router.RouteParameterResolverInterface;

public class CapitalizerResolver implements RouteParameterResolverInterface<String>
{
    public CapitalizerResolver()
    {
    }

    @Override
    public String resolve(String name, String value)
    {
        return value.toUpperCase();
    }
}
