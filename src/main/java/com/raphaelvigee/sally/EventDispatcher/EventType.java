package com.raphaelvigee.sally.EventDispatcher;

import java.util.Objects;

public class EventType<E extends EventInterface>
{
    String name;

    public EventType(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public int hashCode()
    {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof EventType) {
            EventType eventType = (EventType) o;

            return Objects.equals(this.name, eventType.name);
        }

        return false;
    }
}
