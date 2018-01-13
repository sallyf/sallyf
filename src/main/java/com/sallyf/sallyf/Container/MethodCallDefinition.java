package com.sallyf.sallyf.Container;

public class MethodCallDefinition
{
    private String name;

    private ReferenceInterface[] args;

    public MethodCallDefinition(String name, ReferenceInterface... args)
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
