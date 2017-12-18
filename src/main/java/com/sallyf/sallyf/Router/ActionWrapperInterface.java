package com.sallyf.sallyf.Router;

import com.sallyf.sallyf.Exception.UnhandledParameterException;
import org.eclipse.jetty.server.Request;

@FunctionalInterface
public interface ActionWrapperInterface
{
    Response apply(Request request, Route route) throws UnhandledParameterException;
}
