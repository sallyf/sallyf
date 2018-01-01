package com.raphaelvigee.sally.Exception;

import com.raphaelvigee.sally.Server.Status;

public class HttpException extends FrameworkException
{
    private Status status;

    public HttpException(Status status)
    {
        this.status = status;
    }

    public Status getStatus()
    {
        return status;
    }
}
