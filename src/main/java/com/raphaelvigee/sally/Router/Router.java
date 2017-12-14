package com.raphaelvigee.sally.Router;

import com.raphaelvigee.sally.BaseController;
import com.raphaelvigee.sally.Container.Container;
import com.raphaelvigee.sally.Container.ContainerAware;
import com.raphaelvigee.sally.Event.ActionFilterEvent;
import com.raphaelvigee.sally.Event.RouteParametersEvent;
import com.raphaelvigee.sally.EventDispatcher.EventDispatcher;
import com.raphaelvigee.sally.Exception.FrameworkException;
import com.raphaelvigee.sally.Exception.RouteDuplicateException;
import com.raphaelvigee.sally.Exception.UnhandledParameterException;
import com.raphaelvigee.sally.KernelEvents;
import com.raphaelvigee.sally.Server.HTTPSession;
import com.raphaelvigee.sally.Server.Method;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Router extends ContainerAware
{
    private ArrayList<Route> routes = new ArrayList<>();

    private ArrayList<RouteParameterResolverInterface> routeParameterResolvers = new ArrayList<>();

    private ArrayList<String> routeSignatures = new ArrayList<>();

    public void addController(Class<? extends BaseController> controllerClass) throws FrameworkException
    {
        EventDispatcher eventDispatcher = getContainer().get(EventDispatcher.class);

        com.raphaelvigee.sally.Annotation.Route controllerAnnotation = controllerClass.getAnnotation(com.raphaelvigee.sally.Annotation.Route.class);

        String pathPrefix = controllerAnnotation == null ? "" : controllerAnnotation.path();

        java.lang.reflect.Method[] methods = controllerClass.getMethods();

        for (java.lang.reflect.Method method : methods) {
            if (method.isAnnotationPresent(com.raphaelvigee.sally.Annotation.Route.class)) {
                if (!java.lang.reflect.Modifier.isStatic(method.getModifiers())) {
                    System.err.println("Method `" + method.getName() + "` is not static, ignoring");
                    continue;
                }

                com.raphaelvigee.sally.Annotation.Route routeAnnotation = method.getAnnotation(com.raphaelvigee.sally.Annotation.Route.class);

                final Class<?>[] parameterTypes = method.getParameterTypes();

                final ActionInvokerInterface actionInvoker = (parameters) -> {
                    try {
                        return (Response) method.invoke(null, parameters);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                        return null;
                    }
                };

                addAction(routeAnnotation.method(), pathPrefix + routeAnnotation.path(), (session) -> {
                    Object[] parameters = getActionParameters(parameterTypes, session);

                    ActionFilterEvent actionFilterEvent = new ActionFilterEvent(session, parameters, actionInvoker);

                    eventDispatcher.dispatch(KernelEvents.ACTION_FILTER, actionFilterEvent);

                    return actionFilterEvent.getActionInvoker().invoke(actionFilterEvent.getParameters());
                });
            }
        }
    }

    public Object[] getActionParameters(Class<?>[] parameterTypes, HTTPSession session) throws UnhandledParameterException
    {
        Object[] parameters = new Object[parameterTypes.length];
        int i = 0;
        for (Class<?> parameterType : parameterTypes) {
            Object p;

            if (parameterType == Container.class) {
                p = getContainer();
            } else if (parameterType == HTTPSession.class) {
                p = session;
            } else if (parameterType == RouteParameters.class) {
                p = getRouteParameters(session.getRoute(), session);
            } else {
                throw new UnhandledParameterException(parameterType);
            }

            parameters[i++] = p;
        }

        return parameters;
    }

    public void addRoute(Route route) throws RouteDuplicateException
    {
        if (routeSignatures.contains(route.toString())) {
            throw new RouteDuplicateException(route);
        }

        routes.add(route);
        routeSignatures.add(route.toString());
    }

    public void addAction(Method method, String path, ActionWrapperInterface handler) throws RouteDuplicateException
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

    public RouteParameters getRouteParameters(Route route, HTTPSession session)
    {
        Pattern r = Pattern.compile(route.getPath().getPattern());

        Matcher m = r.matcher(session.getUri());

        RouteParameters parameterValues = new RouteParameters();

        if (m.matches()) {
            route.getPath().parameters.forEach((index, name) -> {
                parameterValues.put(name, resolveRouteParameter(m, index, name, session));
            });
        }

        EventDispatcher eventDispatcher = getContainer().get(EventDispatcher.class);

        eventDispatcher.dispatch(KernelEvents.ROUTE_PARAMETERS, new RouteParametersEvent(session, parameterValues));

        return parameterValues;
    }

    public Object resolveRouteParameter(Matcher m, Integer index, String name, HTTPSession session)
    {
        String value = m.group(index);
        Object resolvedValue = value;

        for (RouteParameterResolverInterface resolver : routeParameterResolvers) {
            if (resolver.supports(name, value, session)) {
                resolvedValue = resolver.resolve(name, value, session);
                break;
            }
        }

        return resolvedValue;
    }

    public void addRouteParameterResolver(RouteParameterResolverInterface resolver)
    {
        routeParameterResolvers.add(resolver);
    }
}
