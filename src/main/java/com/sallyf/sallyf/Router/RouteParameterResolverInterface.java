package com.sallyf.sallyf.Router;

import com.sallyf.sallyf.Container.ServiceInterface;

public interface RouteParameterResolverInterface<R> extends ServiceInterface
{
    R resolve(String name, String value);
}
