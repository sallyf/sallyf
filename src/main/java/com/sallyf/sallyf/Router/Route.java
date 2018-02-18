package com.sallyf.sallyf.Router;

import com.sallyf.sallyf.Server.Method;

import java.util.Objects;

public class Route implements Cloneable
{
    private Method method;

    private Path path;

    private ActionWrapperInterface handler;

    private String name;

    public Route(Method method, String pathDeclaration, ActionWrapperInterface handler)
    {
        this.method = method;
        this.path = new Path(pathDeclaration);
        this.handler = handler;
    }

    public Route(String name, Method method, String pathDeclaration, ActionWrapperInterface handler)
    {
        this.method = method;
        this.path = new Path(pathDeclaration);
        this.handler = handler;
        this.name = name;
    }

    public Method getMethod()
    {
        return method;
    }

    public Path getPath()
    {
        return path;
    }

    public ActionWrapperInterface getHandler()
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

    public void setHandler(ActionWrapperInterface handler)
    {
        this.handler = handler;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return String.format("%s %s", getMethod(), getPath().getDeclaration());
    }

    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Route route = (Route) o;
        return getMethod() == route.getMethod() &&
                Objects.equals(getPath(), route.getPath()) &&
                Objects.equals(getName(), route.getName());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getMethod(), getPath(), getName());
    }
}
