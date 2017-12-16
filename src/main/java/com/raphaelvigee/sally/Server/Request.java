package com.raphaelvigee.sally.Server;

import com.raphaelvigee.sally.Router.Route;
import fi.iki.elonen.NanoHTTPD;

import java.util.Map;

public class Request
{
    private String uri;

    private NanoHTTPD.Method method;

    private Map<String, String> parms;

    private Map<String, String> headers;

    private NanoHTTPD.CookieHandler cookies;

    private String queryParameterString;

    private Route route;

    public static Request create(NanoHTTPD.IHTTPSession session)
    {
        Request r = new Request();
        r.setCookies(session.getCookies());
        r.setHeaders(session.getHeaders());
        r.setMethod(session.getMethod());
        r.setParms(session.getParms());
        r.setUri(session.getUri());
        r.setQueryParameterString(session.getQueryParameterString());

        return r;
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

    public Route getRoute()
    {
        return route;
    }

    public void setRoute(Route route)
    {
        this.route = route;
    }
}
