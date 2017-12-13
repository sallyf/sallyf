package com.raphaelvigee.sally.Router;

import com.raphaelvigee.sally.Container.Container;
import com.raphaelvigee.sally.Exception.UnhandledParameterException;
import com.raphaelvigee.sally.Server.HTTPSession;

public interface ActionInterface
{
    Response apply(Container c, HTTPSession s, Route r) throws UnhandledParameterException;
}
