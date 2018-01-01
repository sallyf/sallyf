package com.raphaelvigee.sally.Router;

import com.raphaelvigee.sally.Exception.HttpException;
import com.raphaelvigee.sally.Exception.UnhandledParameterException;
import com.raphaelvigee.sally.Server.RuntimeBag;

@FunctionalInterface
public interface ActionWrapperInterface
{
    Object apply(RuntimeBag runtimeBag) throws UnhandledParameterException, HttpException;
}
