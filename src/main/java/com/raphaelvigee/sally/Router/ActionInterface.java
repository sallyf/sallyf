package com.raphaelvigee.sally.Router;

@FunctionalInterface
public interface ActionInterface
{
    Response apply(Object... args);
}
