package com.sallyf.sallyf.Container;

public class PlainReference implements ReferenceInterface
{
    Object value;

    Class type;

    public PlainReference(Object value)
    {
        this.value = value;
        this.type = value.getClass();
    }

    public PlainReference(Object value, Class type)
    {
        this.value = value;
        this.type = type;
    }
}
