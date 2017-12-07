package com.raphaelvigee.sally;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            if (session.getMethod().toString().equals(route.getMethod().toString())) {
                Pattern r = Pattern.compile(route.getPath().pattern);

                Matcher m = r.matcher(session.getUri());

                if (m.matches()) {
                    return route;
                }
            }
        }

        return null;
    }
}
