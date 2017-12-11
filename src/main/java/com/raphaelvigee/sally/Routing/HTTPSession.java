package com.raphaelvigee.sally.Routing;

import fi.iki.elonen.NanoHTTPD;

import java.util.Map;

public class HTTPSession
{
    private String uri;

    private NanoHTTPD.Method method;

    private Map<String, String> parms;

    private Map<String, String> headers;

    private NanoHTTPD.CookieHandler cookies;

    private String queryParameterString;

    public static HTTPSession create(NanoHTTPD.IHTTPSession session)
    {
        HTTPSession s = new HTTPSession();
        s.setCookies(session.getCookies());
        s.setHeaders(session.getHeaders());
        s.setMethod(session.getMethod());
        s.setParms(session.getParms());
        s.setUri(session.getUri());
        s.setQueryParameterString(session.getQueryParameterString());

        return s;
    }

    public String getUri()
    {
        return uri;
    }

    public void setUri(String uri)
    {
        this.uri = uri;
    }

    public NanoHTTPD.Method getMethod()
    {
        return method;
    }

    public void setMethod(NanoHTTPD.Method method)
    {
        this.method = method;
    }

    public Map<String, String> getParms()
    {
        return parms;
    }

    public void setParms(Map<String, String> parms)
    {
        this.parms = parms;
    }

    public Map<String, String> getHeaders()
    {
        return headers;
    }

    public void setHeaders(Map<String, String> headers)
    {
        this.headers = headers;
    }

    public NanoHTTPD.CookieHandler getCookies()
    {
        return cookies;
    }

    public void setCookies(NanoHTTPD.CookieHandler cookies)
    {
        this.cookies = cookies;
    }

    public String getQueryParameterString()
    {
        return queryParameterString;
    }

    public void setQueryParameterString(String queryParameterString)
    {
        this.queryParameterString = queryParameterString;
    }
}
