package com.raphaelvigee.sally.Exception;

import com.raphaelvigee.sally.Routing.Route;

public class RouteDuplicateException extends FrameworkException
{
    public RouteDuplicateException(Route route)
    {
        super("Route already present: " + route);
    }
}
