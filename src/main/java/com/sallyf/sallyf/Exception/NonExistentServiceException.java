package com.sallyf.sallyf.Exception;

public class NonExistentServiceException extends Exception
{
    public NonExistentServiceException(Class type)
    {
        super("Non existent service: " + type.getName());
    }
}
