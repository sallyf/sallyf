package com.sallyf.sallyf.Routing;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Exception.UnhandledParameterException;

public interface ActionInterface
{
    Response apply(Container c, HTTPSession s, Route r) throws UnhandledParameterException;
}
