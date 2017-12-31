package com.raphaelvigee.sally.Router;

import com.raphaelvigee.sally.Server.Status;

import java.util.ArrayList;
import java.util.HashMap;

public class Response
{
    String content;

    Status status;

    String mimeType;

    HashMap<String, ArrayList<String>> headers = new HashMap<>();

    public Response()
    {
        this.status = Status.OK;
        this.mimeType = "text/html";
    }

    public Response(String content)
    {
        this();
        this.content = content;
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

    public void addHeader(String name, String value)
    {
        if(!headers.containsKey(name)) {
            headers.put(name, new ArrayList<>());
        }

        getHeaders(name).add(value);
    }

    public HashMap<String, ArrayList<String>> getHeaders()
    {
        return headers;
    }

    public ArrayList<String> getHeaders(String name)
    {
        return headers.get(name);
    }
}
