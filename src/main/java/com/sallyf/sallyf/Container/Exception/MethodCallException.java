package com.sallyf.sallyf.Container.Exception;

public class MethodCallException extends ServiceInstantiationException
{
    public MethodCallException()
    {
    }

    public MethodCallException(String s)
    {
        super(s);
    }

    public MethodCallException(Throwable throwable)
    {
        super(throwable);
    }
}
