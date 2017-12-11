package com.sallyf.sallyf.Routing;

import com.sallyf.sallyf.BaseController;
import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ContainerAware;
import com.sallyf.sallyf.Exception.FrameworkException;
import com.sallyf.sallyf.Exception.RouteDuplicateException;
import com.sallyf.sallyf.Exception.UnhandledParameterException;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Routing extends ContainerAware
{
    private ArrayList<Route> routes = new ArrayList<>();

    private ArrayList<String> routeSignatures = new ArrayList<>();

    public void addController(Class<? extends BaseController> controllerClass) throws FrameworkException
    {
        java.lang.reflect.Method[] methods = controllerClass.getMethods();

        for (java.lang.reflect.Method method : methods) {
            if (method.isAnnotationPresent(com.sallyf.sallyf.Annotation.Route.class)) {
                if (!java.lang.reflect.Modifier.isStatic(method.getModifiers())) {
                    System.err.println("Method `" + method.getName() + "` is not static, ignoring");
                    continue;
                }

                com.sallyf.sallyf.Annotation.Route route = method.getAnnotation(com.sallyf.sallyf.Annotation.Route.class);

                final Class<?>[] parameterTypes = method.getParameterTypes();

                addAction(route.method(), route.path(), (container, session, routeDefinition) -> {
                    Object[] parameters = new Object[parameterTypes.length];
                    for (Class<?> parameterType : parameterTypes) {
                        Object p;
                        if (parameterType == Container.class) {
                            p = container;
                        } else if (parameterType == HTTPSession.class) {
                            p = session;
                        } else if (parameterType == Route.class) {
                            p = routeDefinition;
                        } else {
                            throw new UnhandledParameterException(parameterType);
                        }
                        parameters[parameters.length] = p;
                    }

                    try {
                        return (Response) method.invoke(null, parameters);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                        return null;
                    }
                });
            }
        }
    }

    public void addRoute(Route route) throws RouteDuplicateException
    {
        if (routeSignatures.contains(route.toString())) {
            throw new RouteDuplicateException(route);
        }

        routes.add(route);
        routeSignatures.add(route.toString());
    }

    public void addAction(Method method, String path, ActionInterface handler) throws RouteDuplicateException
    {
        addRoute(new Route(method, path, handler));
    }

    public void get(String path, ActionInterface handler) throws RouteDuplicateException
    {
        addAction(Method.GET, path, handler);
    }

    public void post(String path, ActionInterface handler) throws RouteDuplicateException
    {
        addAction(Method.POST, path, handler);
    }

    public void put(String path, ActionInterface handler) throws RouteDuplicateException
    {
        addAction(Method.PUT, path, handler);
    }

    public void patch(String path, ActionInterface handler) throws RouteDuplicateException
    {
        addAction(Method.PATCH, path, handler);
    }

    public void delete(String path, ActionInterface handler) throws RouteDuplicateException
    {
        addAction(Method.DELETE, path, handler);
    }

    public ArrayList<Route> getRoutes()
    {
        return routes;
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
