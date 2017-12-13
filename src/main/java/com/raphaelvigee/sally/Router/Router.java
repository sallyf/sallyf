package com.raphaelvigee.sally.Router;

import com.raphaelvigee.sally.BaseController;
import com.raphaelvigee.sally.Container.Container;
import com.raphaelvigee.sally.Container.ContainerAware;
import com.raphaelvigee.sally.Exception.FrameworkException;
import com.raphaelvigee.sally.Exception.RouteDuplicateException;
import com.raphaelvigee.sally.Exception.UnhandledParameterException;
import com.raphaelvigee.sally.Server.HTTPSession;
import com.raphaelvigee.sally.Server.Method;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Router extends ContainerAware
{
    private ArrayList<Route> routes = new ArrayList<>();

    private ArrayList<String> routeSignatures = new ArrayList<>();

    public void addController(Class<? extends BaseController> controllerClass) throws FrameworkException
    {
        com.raphaelvigee.sally.Annotation.Route controllerClassAnnotation = controllerClass.getAnnotation(com.raphaelvigee.sally.Annotation.Route.class);

        String pathPrefix = controllerClassAnnotation == null ? "" : controllerClassAnnotation.path();

        java.lang.reflect.Method[] methods = controllerClass.getMethods();

        for (java.lang.reflect.Method method : methods) {
            if (method.isAnnotationPresent(com.raphaelvigee.sally.Annotation.Route.class)) {
                if (!java.lang.reflect.Modifier.isStatic(method.getModifiers())) {
                    System.err.println("Method `" + method.getName() + "` is not static, ignoring");
                    continue;
                }

                com.raphaelvigee.sally.Annotation.Route routeAnnotation = method.getAnnotation(com.raphaelvigee.sally.Annotation.Route.class);

                final Class<?>[] parameterTypes = method.getParameterTypes();

                addAction(routeAnnotation.method(), pathPrefix + routeAnnotation.path(), (container, session, routeDefinition) -> {
                    Object[] parameters = new Object[parameterTypes.length];
                    int i = 0;
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
                        parameters[i++] = p;
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
