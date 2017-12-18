package com.raphaelvigee.sally.Router;

import com.raphaelvigee.sally.Exception.UnhandledParameterException;
import org.eclipse.jetty.server.Request;

@FunctionalInterface
public interface ActionWrapperInterface
{
    Response apply(Request request, Route route) throws UnhandledParameterException;
}
