package com.sallyf.sallyf.Container;

public class CallDefinition
{
    String name;

    ReferenceInterface[] args;

    public CallDefinition(String name, ReferenceInterface... args)
    {
        this.name = name;
        this.args = args;
    }

}
