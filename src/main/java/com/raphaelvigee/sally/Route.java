package com.raphaelvigee.sally;

enum Method
{
    GET("get"), POST("post"), PUT("put"), PATCH("patch"), DELETE("delete");

    private String s;

    Method(String s)
    {
        this.s = s;
    }

    @Override
    public String toString()
    {
        return s;
    }
}

public class Route
{
    private Method method;

    private String path;

    private ActionInterface handler;

    public Route(Method method, String path, ActionInterface handler)
    {
        this.method = method;
        this.path = path;
        this.handler = handler;
    }

    public Method getMethod()
    {
        return method;
    }

    public void setMethod(Method method)
    {
        this.method = method;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public ActionInterface getHandler()
    {
        return handler;
    }

    public void setHandler(ActionInterface handler)
    {
        this.handler = handler;
    }
}
