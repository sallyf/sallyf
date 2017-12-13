package com.raphaelvigee.sally.EventDispatcher;

@FunctionalInterface
public interface EventHandlerInterface<E extends EventInterface>
{
    void dispatch(E e);
}
