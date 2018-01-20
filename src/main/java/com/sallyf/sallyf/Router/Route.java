package com.sallyf.sallyf.Router;

import com.sallyf.sallyf.Server.Method;

import java.util.Objects;
import java.util.stream.Stream;

public class Route implements Cloneable
{
    private Method[] methods;

    private Path path;

    private ActionWrapperInterface handler;

    private String name;

    public Route(Method[] methods, String pathDeclaration, ActionWrapperInterface handler)
    {
        this.methods = methods;
        this.path = new Path(pathDeclaration);
        this.handler = handler;
    }

    public Route(String name, Method[] methods, String pathDeclaration, ActionWrapperInterface handler)
    {
        this.methods = methods;
        this.path = new Path(pathDeclaration);
        this.handler = handler;
        this.name = name;
    }

    public Method[] getMethods()
    {
        return methods;
    }

    public Path getPath()
    {
        return path;
    }

    public ActionWrapperInterface getHandler()
    {
        return handler;
    }

    public void setMethod(Method[] methods)
    {
        this.methods = methods;
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
        String[] methods = Stream.of(getMethods()).map(Enum::toString).toArray(String[]::new);

        return String.format("%s %s", String.join("|", methods), getPath().declaration);
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
        return getMethods() == route.getMethods() &&
                Objects.equals(getPath(), route.getPath()) &&
                Objects.equals(getName(), route.getName());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getMethods(), getPath(), getName());
    }
}
