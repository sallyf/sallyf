package com.raphaelvigee.sally.Router;

import com.raphaelvigee.sally.Exception.UnhandledParameterException;
import com.raphaelvigee.sally.Server.RuntimeBag;

@FunctionalInterface
public interface ActionWrapperInterface
{
    Response apply(RuntimeBag runtimeBag) throws UnhandledParameterException;
}
