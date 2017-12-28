package com.sallyf.sallyf.Router;

import com.sallyf.sallyf.Server.Status;
import org.eclipse.jetty.http.HttpFields;

import java.util.Collection;
import java.util.Collections;

public class Response
{
    String content;

    Status status;

    String mimeType;

    HttpFields httpFields = new HttpFields();

    public Response()
    {

    }

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

    public void addHeader(String name, String value)
    {
        httpFields.add(name, value);
    }

    public Collection<String> getHeaderNames()
    {
        return httpFields.getFieldNamesCollection();
    }

    public String getHeader(String name)
    {
        return httpFields.get(name);
    }

    public Collection<String> getHeaders(String name)
    {
        Collection<String> i = httpFields.getValuesList(name);
        if (i == null)
            return Collections.emptyList();
        return i;
    }
}
