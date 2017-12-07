package com.raphaelvigee.sally;

import java.util.function.Function;

@FunctionalInterface
public interface ActionInterface extends Function<HTTPSession, Response>
{
}
