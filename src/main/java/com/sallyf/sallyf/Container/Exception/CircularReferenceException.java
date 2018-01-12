package com.sallyf.sallyf.Container.Exception;

public class CircularReferenceException extends ServiceInstantiationException
{
    public CircularReferenceException(String path)
    {
        super("Circular reference: " + path);
    }
}
