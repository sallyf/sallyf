package com.sallyf.sallyf.Router;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Exception.UnhandledParameterException;
import com.sallyf.sallyf.Server.HTTPSession;

@FunctionalInterface
public interface ActionInterface
{
    Response apply(Container c, HTTPSession s, Route r) throws UnhandledParameterException;
}
