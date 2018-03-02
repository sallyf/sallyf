package com.sallyf.sallyf.Exception;

import java.lang.reflect.Parameter;

public class UnhandledParameterException extends FrameworkException
{
    public UnhandledParameterException(Parameter parameter)
    {
        super("Unhandled parameter " + parameter.getType().getCanonicalName());
    }
}
