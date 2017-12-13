package com.raphaelvigee.sally.Router;

import com.raphaelvigee.sally.Exception.UnhandledParameterException;
import com.raphaelvigee.sally.Server.HTTPSession;

@FunctionalInterface
public interface ActionWrapperInterface
{
    Response apply(HTTPSession s) throws UnhandledParameterException;
}
