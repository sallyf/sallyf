package com.sallyf.sallyf;

import java.util.ArrayList;
import java.util.Objects;

public class Routing extends ContainerAware
{
    private ArrayList<Route> routes = new ArrayList<>();

    public void addRoute(Route route)
    {
        routes.add(route);
    }

    public void addAction(Method method, String path, ActionInterface handler)
    {
        addRoute(new Route(method, path, handler));
    }

    public void get(String path, ActionInterface handler)
    {
        addAction(Method.GET, path, handler);
    }

    public void post(String path, ActionInterface handler)
    {
        addAction(Method.POST, path, handler);
    }

    public void put(String path, ActionInterface handler)
    {
        addAction(Method.PUT, path, handler);
    }

    public void patch(String path, ActionInterface handler)
    {
        addAction(Method.PATCH, path, handler);
    }

    public void delete(String path, ActionInterface handler)
    {
        addAction(Method.DELETE, path, handler);
    }

    public Route match(HTTPSession session)
    {
        for (Route route : routes) {
            if (Objects.equals(route.getPath(), session.getUri())) {
                return route;
            }
        }

        return null;
    }
}
