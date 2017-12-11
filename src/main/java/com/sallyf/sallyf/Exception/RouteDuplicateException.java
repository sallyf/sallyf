package com.sallyf.sallyf.Exception;

import com.sallyf.sallyf.Route;

public class RouteDuplicateException extends Exception
{
    public RouteDuplicateException(Route route)
    {
        super("Route already present: " + route);
    }
}
