package com.raphaelvigee.sally.Router;

@FunctionalInterface
public interface ActionInvokerInterface
{
    Response invoke(Object[] parameters);
}
