package com.sallyf.sallyf.Container.Exception;

import com.sallyf.sallyf.Exception.FrameworkException;

public class ServiceInstantiationException extends FrameworkException
{
    public ServiceInstantiationException()
    {
    }

    public ServiceInstantiationException(String s)
    {
        super(s);
    }

    public ServiceInstantiationException(Throwable throwable)
    {
        super(throwable);
    }
}
