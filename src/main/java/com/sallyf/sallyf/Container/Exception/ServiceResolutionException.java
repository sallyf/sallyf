package com.sallyf.sallyf.Container.Exception;

public class ServiceResolutionException extends ServiceInstantiationException
{
    private Class type;

    public ServiceResolutionException(Class type)
    {
        this.type = type;
    }
}
