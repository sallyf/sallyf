package com.sallyf.sallyf.Exception;

public class NonExistentServiceException extends FrameworkException
{
    public NonExistentServiceException(Class type)
    {
        super("Non existent service: " + type.getName());
    }
}
