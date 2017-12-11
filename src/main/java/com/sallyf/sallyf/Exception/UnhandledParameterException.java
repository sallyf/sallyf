package com.sallyf.sallyf.Exception;

public class UnhandledParameterException extends FrameworkException
{
    public UnhandledParameterException(Class<?> parameterType)
    {
        super("Unhandled parameter " + parameterType.getCanonicalName());
    }
}
