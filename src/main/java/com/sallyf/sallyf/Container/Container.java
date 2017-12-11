package com.sallyf.sallyf.Container;

import java.util.HashMap;
import java.util.Map;

public class Container
{
    private Map<String, ContainerAwareInterface> services;

    public Container()
    {
        services = new HashMap<>();
    }

    public <T extends ContainerAwareInterface> T add(Class<T> type)
    {
        String name = type.toString();

        T instance;
        try {
            instance = type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }

        instance.setContainer(this);

        services.put(name, instance);

        return instance;
    }

    public <T extends ContainerAwareInterface> T get(Class<T> type)
    {
        return (T) services.get(type.toString());
    }
}
