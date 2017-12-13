package com.raphaelvigee.sally.EventDispatcher;

import com.raphaelvigee.sally.Container.ContainerAware;

import java.util.ArrayList;
import java.util.HashMap;

public class EventDispatcher extends ContainerAware
{
    private HashMap<String, ArrayList<EventHandlerInterface>> events = new HashMap<>();

    public <E extends EventInterface> void register(String name, EventHandlerInterface<E> eventHandler)
    {
        if (events.containsKey(name)) {
            events.get(name).add(eventHandler);
        } else {
            ArrayList<EventHandlerInterface> handlers = new ArrayList<>();
            handlers.add(eventHandler);
            events.put(name, handlers);
        }
    }

    public void dispatch(String name, EventInterface event)
    {
        ArrayList<EventHandlerInterface> handlers = events.get(name);

        if (handlers != null) {
            for (EventHandlerInterface handler : handlers) {
                handler.dispatch(event);
            }
        }
    }

    public void dispatch(String name)
    {
        ArrayList<EventHandlerInterface> handlers = events.get(name);

        if (handlers != null) {
            for (EventHandlerInterface handler : handlers) {
                handler.dispatch(null);
            }
        }
    }
}
