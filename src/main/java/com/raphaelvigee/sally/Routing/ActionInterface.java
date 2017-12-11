package com.raphaelvigee.sally.Routing;

import com.raphaelvigee.sally.Container.Container;
import com.raphaelvigee.sally.Exception.UnhandledParameterException;

public interface ActionInterface
{
    Response apply(Container c, HTTPSession s, Route r) throws UnhandledParameterException;
}
