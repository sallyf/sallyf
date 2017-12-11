package com.raphaelvigee.sally;

import com.raphaelvigee.sally.Exception.UnhandledParameterException;

@FunctionalInterface
public interface ActionHandlerInterface<A, B, C, R>
{
    R apply(A var1, B var2, C var3) throws UnhandledParameterException;
}
