package com.sallyf.sallyf.EventDispatcher;

import com.sallyf.sallyf.Container.ContainerAwareInterface;

import java.util.ArrayList;
import java.util.HashMap;

public class EventDispatcher implements ContainerAwareInterface
{
    private HashMap<EventType, ArrayList<EventHandlerInterface>> events = new HashMap<>();

    public HashMap<EventType, ArrayList<EventHandlerInterface>> getEvents()
    {
        return events;
    }

    public <E extends EventInterface> void register(EventType<E>[] eventTypes, EventHandlerInterface<E> eventHandler)
    {
        for (EventType<E> eventType : eventTypes) {
            register(eventType, eventHandler);
        }
    }

    public <E extends EventInterface> void register(EventType<E> eventType, EventHandlerInterface<E> eventHandler)
    {
        if (events.containsKey(eventType)) {
            events.get(eventType).add(eventHandler);
        } else {
            ArrayList<EventHandlerInterface> handlers = new ArrayList<>();
            handlers.add(eventHandler);
            events.put(eventType, handlers);
        }
    }

    public <E extends EventInterface> E dispatch(EventType<E> eventType, E event)
    {
        ArrayList<EventHandlerInterface> handlers = events.get(eventType);

        if (handlers != null) {
            for (EventHandlerInterface<E> handler : handlers) {
                handler.dispatch(eventType, event);
            }
        }

        return event;
    }

    public void dispatch(EventType eventType)
    {
        dispatch(eventType, null);
    }
}
