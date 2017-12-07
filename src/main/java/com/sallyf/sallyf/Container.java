package com.sallyf.sallyf;

import java.util.HashMap;
import java.util.Map;

public class Container
{
    private Map<String, ContainerAwareInterface> services;

    public Container()
    {
        services = new HashMap<>();
    }

    public <T extends ContainerAwareInterface> void add(Class<T> type)
    {
        String name = type.toString();

        T instance;
        try {
            instance = type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return;
        }

        instance.setContainer(this);

        services.put(name, instance);
    }

    public <T extends ContainerAwareInterface> T get(Class<T> type)
    {
        return (T) services.get(type.toString());
    }
}
