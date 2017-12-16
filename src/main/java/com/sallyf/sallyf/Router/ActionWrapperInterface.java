package com.sallyf.sallyf.Router;

import com.sallyf.sallyf.Exception.UnhandledParameterException;
import com.sallyf.sallyf.Server.Request;

@FunctionalInterface
public interface ActionWrapperInterface
{
    Response apply(Request r) throws UnhandledParameterException;
}
