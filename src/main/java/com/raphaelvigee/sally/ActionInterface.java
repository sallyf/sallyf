package com.raphaelvigee.sally;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ActionInterface extends BiFunction<HTTPSession, Route, Response>
{
}
