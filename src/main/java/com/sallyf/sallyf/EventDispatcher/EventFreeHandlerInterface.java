package com.sallyf.sallyf.EventDispatcher;

@FunctionalInterface
public interface EventFreeHandlerInterface
{
    void dispatch(EventType eventType);
}
