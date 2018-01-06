package com.sallyf.sallyf.Container.Exception;

public class ReferenceResolutionException extends ServiceInstantiationException
{
    public ReferenceResolutionException(String s)
    {
        super(s);
    }

    public ReferenceResolutionException(Throwable throwable)
    {
        super(throwable);
    }
}
