package com.raphaelvigee.sally.Router;

import com.raphaelvigee.sally.Exception.UnhandledParameterException;
import com.raphaelvigee.sally.Server.Request;

@FunctionalInterface
public interface ActionWrapperInterface
{
    Response apply(Request r) throws UnhandledParameterException;
}
