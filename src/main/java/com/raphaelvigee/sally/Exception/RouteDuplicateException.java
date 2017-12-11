package com.raphaelvigee.sally.Exception;

import com.raphaelvigee.sally.Route;

public class RouteDuplicateException extends Exception
{
    public RouteDuplicateException(Route route)
    {
        super("Route already present: " + route);
    }
}
