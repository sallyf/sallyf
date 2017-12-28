package com.raphaelvigee.sally.Exception;

public class InvalidResponseTypeException extends FrameworkException
{
    public InvalidResponseTypeException(Object response)
    {
        super("Invalid response type: " + response.getClass());
    }
}
