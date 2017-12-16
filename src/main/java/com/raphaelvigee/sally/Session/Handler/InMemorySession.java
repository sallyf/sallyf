package com.raphaelvigee.sally.Session.Handler;

import com.raphaelvigee.sally.Session.SessionInterface;

import java.util.HashMap;

public class InMemorySession implements SessionInterface<Object>
{
    private String id;

    private HashMap<String, Object> elements = new HashMap<>();

    public InMemorySession(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }

    @Override
    public Object get(String name)
    {
        return elements.get(name);
    }

    @Override
    public void set(String name, Object value)
    {
        elements.put(name, value);
    }

    @Override
    public HashMap<String, Object> getAll()
    {
        return elements;
    }
}
