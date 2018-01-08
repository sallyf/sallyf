package com.sallyf.sallyf.Container.Exception;

public class CallException extends ServiceInstantiationException
{
    public CallException()
    {
    }

    public CallException(String s)
    {
        super(s);
    }

    public CallException(Throwable throwable)
    {
        super(throwable);
    }
}
