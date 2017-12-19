package com.sallyf.sallyf.EventDispatcher;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ContainerAware;

import java.util.ArrayList;
import java.util.HashMap;

public class EventDispatcher extends ContainerAware
{
    private HashMap<EventType, ArrayList<EventHandlerInterface>> events = new HashMap<>();

    public HashMap<EventType, ArrayList<EventHandlerInterface>> getEvents()
    {
        return events;
    }

    public EventDispatcher(Container container)
    {
        super(container);
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

    public <E extends EventInterface> void dispatch(EventType<E> eventType, E event)
    {
        ArrayList<EventHandlerInterface> handlers = events.get(eventType);

        if (handlers != null) {
            for (EventHandlerInterface<E> handler : handlers) {
                handler.dispatch(event);
            }
        }
    }

    public void dispatch(EventType eventType)
    {
        ArrayList<EventHandlerInterface> handlers = events.get(eventType);

        if (handlers != null) {
            for (EventHandlerInterface handler : handlers) {
                handler.dispatch(null);
            }
        }
    }
}
