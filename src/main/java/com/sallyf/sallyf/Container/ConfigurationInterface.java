package com.sallyf.sallyf.Container;

public interface ConfigurationInterface
{
    Object get(Object key);

    Object has(Object key);

    Object set(Object key, Object value);
}
