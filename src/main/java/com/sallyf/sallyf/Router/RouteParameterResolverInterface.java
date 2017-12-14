package com.sallyf.sallyf.Router;

import com.sallyf.sallyf.Server.HTTPSession;

public interface RouteParameterResolverInterface<R>
{
    boolean supports(String name, String value, HTTPSession session);

    R resolve(String name, String value, HTTPSession session);
}
