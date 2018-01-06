package com.sallyf.sallyf.Container;

public class PlainReference<V> implements ReferenceInterface
{
    public V value;

    public PlainReference(V value)
    {
        this.value = value;
    }
}
