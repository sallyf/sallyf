package com.sallyf.sallyf.Router;

import com.sallyf.sallyf.Container.ServiceInterface;
import com.sallyf.sallyf.Server.RuntimeBag;

public interface RouteParameterResolverInterface<R> extends ServiceInterface
{
    R resolve(String name, String value, RuntimeBag runtimeBag);
}
