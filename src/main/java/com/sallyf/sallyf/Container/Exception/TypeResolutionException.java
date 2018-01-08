package com.sallyf.sallyf.Container.Exception;

public class TypeResolutionException extends ServiceInstantiationException
{
    public TypeResolutionException(String s)
    {
        super(s);
    }

    public TypeResolutionException(Throwable throwable)
    {
        super(throwable);
    }
}
