package com.sallyf.sallyf.Router;

import org.eclipse.jetty.server.Request;

public interface RouteParameterResolverInterface<R>
{
    boolean supports(String name, String value, Request request, Route route);

    R resolve(String name, String value, Request request, Route route);
}
