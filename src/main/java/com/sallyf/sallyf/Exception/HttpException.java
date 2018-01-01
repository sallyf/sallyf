package com.sallyf.sallyf.Exception;

import com.sallyf.sallyf.Server.Status;

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
