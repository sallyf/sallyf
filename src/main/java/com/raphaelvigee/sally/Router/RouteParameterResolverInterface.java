package com.raphaelvigee.sally.Router;

import com.raphaelvigee.sally.Server.HTTPSession;

public interface RouteParameterResolverInterface<R>
{
    boolean supports(String name, String value, HTTPSession session);

    R resolve(String name, String value, HTTPSession session);
}
