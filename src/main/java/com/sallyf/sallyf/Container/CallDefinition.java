package com.sallyf.sallyf.Container;

public class CallDefinition
{
    private String name;

    private ReferenceInterface[] args;

    public CallDefinition(String name, ReferenceInterface... args)
    {
        this.name = name;
        this.args = args;
    }

    public String getName()
    {
        return name;
    }

    public ReferenceInterface[] getArgs()
    {
        return args;
    }
}
