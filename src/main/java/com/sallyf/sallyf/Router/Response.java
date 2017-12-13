package com.sallyf.sallyf.Router;

import com.sallyf.sallyf.Server.Status;

public class Response
{
    String content;

    Status status;

    String mimeType;

    public Response(String content)
    {
        this.content = content;
        this.status = Status.OK;
        this.mimeType = "text/html";
    }

    public Response(String content, Status status, String mimeType)
    {
        this.content = content;
        this.status = status;
        this.mimeType = mimeType;
    }

    public String getContent()
    {
        return content;
    }

    public Status getStatus()
    {
        return status;
    }

    public String getMimeType()
    {
        return mimeType;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }

    public void setMimeType(String mimeType)
    {
        this.mimeType = mimeType;
    }
}
