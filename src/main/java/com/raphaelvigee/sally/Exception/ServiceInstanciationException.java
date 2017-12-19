package com.raphaelvigee.sally.Exception;

public class ServiceInstanciationException extends FrameworkException
{
    public ServiceInstanciationException(String s)
    {
        super(s);
    }

    public ServiceInstanciationException(Throwable throwable)
    {
        super(throwable);
    }
}
