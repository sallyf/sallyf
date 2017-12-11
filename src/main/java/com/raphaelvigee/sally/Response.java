package com.raphaelvigee.sally;

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
}
