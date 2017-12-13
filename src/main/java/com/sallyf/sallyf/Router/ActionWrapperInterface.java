package com.sallyf.sallyf.Router;

import com.sallyf.sallyf.Exception.UnhandledParameterException;
import com.sallyf.sallyf.Server.HTTPSession;

@FunctionalInterface
public interface ActionWrapperInterface
{
    Response apply(HTTPSession s) throws UnhandledParameterException;
}
