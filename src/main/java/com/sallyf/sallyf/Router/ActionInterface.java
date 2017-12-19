package com.sallyf.sallyf.Router;

@FunctionalInterface
public interface ActionInterface
{
    Response apply(Object... args);
}
