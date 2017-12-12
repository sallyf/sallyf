package com.sallyf.sallyf.Exception;

import com.sallyf.sallyf.Router.Route;

public class RouteDuplicateException extends FrameworkException
{
    public RouteDuplicateException(Route route)
    {
        super("Route already present: " + route);
    }
}
