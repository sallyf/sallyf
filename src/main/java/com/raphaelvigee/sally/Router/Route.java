package com.raphaelvigee.sally.Router;

import com.raphaelvigee.sally.Server.HTTPSession;
import com.raphaelvigee.sally.Server.Method;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Route
{
    private Method method;

    private Path path;

    private ActionInterface handler;

    public Route(Method method, String pathDeclaration, ActionInterface handler)
    {
        this.method = method;
        this.path = new Path(pathDeclaration);
        this.handler = handler;
    }

    public Method getMethod()
    {
        return method;
    }

    public Path getPath()
    {
        return path;
    }

    public ActionInterface getHandler()
    {
        return handler;
    }

    public void setMethod(Method method)
    {
        this.method = method;
    }

    public void setPath(Path path)
    {
        this.path = path;
    }

    public void setHandler(ActionInterface handler)
    {
        this.handler = handler;
    }

    @Override
    public String toString()
    {
        return String.format("%s %s", getMethod(), getPath().declaration);
    }
}
