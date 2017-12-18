package com.sallyf.sallyf.Router;

import com.sallyf.sallyf.Exception.UnhandledParameterException;
import com.sallyf.sallyf.Server.RuntimeBag;

@FunctionalInterface
public interface ActionWrapperInterface
{
    Response apply(RuntimeBag runtimeBag) throws UnhandledParameterException;
}
