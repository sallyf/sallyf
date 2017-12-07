package com.sallyf.sallyf;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ActionInterface extends BiFunction<HTTPSession, Route, Response>
{
}
