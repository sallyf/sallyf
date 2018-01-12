package com.sallyf.sallyf.Container;

public class ConstructorDefinition
{
    private ReferenceInterface[] args;

    public ConstructorDefinition(ReferenceInterface... args)
    {
        this.args = args;
    }

    public ReferenceInterface[] getArgs()
    {
        return args;
    }
}
