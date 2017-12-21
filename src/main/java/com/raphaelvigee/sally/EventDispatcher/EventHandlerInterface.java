package com.raphaelvigee.sally.EventDispatcher;

@FunctionalInterface
public interface EventHandlerInterface<E extends EventInterface>
{
    void dispatch(EventType<E> eventType, E e);
}
