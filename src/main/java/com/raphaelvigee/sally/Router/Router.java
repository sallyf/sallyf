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
import com.raphaelvigee.sally.Server.Method;
import com.raphaelvigee.sally.Server.RuntimeBag;
import org.eclipse.jetty.server.Request;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Router extends ContainerAware
{
    public Router(Container container)
    {
        super(container);
    }

    private HashMap<String, Route> routes = new HashMap<>();

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

                String actionName = controllerClass.getSimpleName() + "." + method.getName();

                addAction(actionName, routeAnnotation.method(), pathPrefix + routeAnnotation.path(), (runtimeBag) -> {
                    Object[] parameters = getActionParameters(parameterTypes, runtimeBag);

                    ActionFilterEvent actionFilterEvent = new ActionFilterEvent(runtimeBag, parameters, actionInvoker);

                    eventDispatcher.dispatch(KernelEvents.ACTION_FILTER, actionFilterEvent);

                    return actionFilterEvent.getActionInvoker().invoke(actionFilterEvent.getParameters());
                });
            }
        }
    }

    public Object[] getActionParameters(Class<?>[] parameterTypes, RuntimeBag runtimeBag) throws UnhandledParameterException
    {
        Object[] parameters = new Object[parameterTypes.length];
        int i = 0;
        for (Class<?> parameterType : parameterTypes) {
            Object p;

            if (parameterType == Container.class) {
                p = getContainer();
            } else if (parameterType == Request.class) {
                p = runtimeBag.getRequest();
            } else if (parameterType == RouteParameters.class) {
                p = getRouteParameters(runtimeBag);
            } else {
                throw new UnhandledParameterException(parameterType);
            }

            parameters[i++] = p;
        }

        return parameters;
    }

    public void addRoute(String name, Route route) throws RouteDuplicateException
    {
        String signature = route.getMethod() + " " + route.getPath().getPattern();
        if (routeSignatures.contains(signature)) {
            throw new RouteDuplicateException(route);
        }

        route.setName(name);

        routes.put(name, route);
        routeSignatures.add(signature);
    }

    public void addAction(String name, Method method, String path, ActionWrapperInterface handler) throws RouteDuplicateException
    {
        addRoute(name, new Route(name, method, path, handler));
    }

    public HashMap<String, Route> getRoutes()
    {
        return routes;
    }

    public Route match(Request request)
    {
        for (Route route : routes.values()) {
            if (request.getMethod().equals(route.getMethod().toString())) {
                Pattern r = Pattern.compile(route.getPath().pattern);

                Matcher m = r.matcher(request.getPathInfo());

                if (m.matches()) {
                    return route;
                }
            }
        }

        return null;
    }

    public RouteParameters getRouteParameters(RuntimeBag runtimeBag)
    {
        Pattern r = Pattern.compile(runtimeBag.getRoute().getPath().getPattern());

        Matcher m = r.matcher(runtimeBag.getRequest().getPathInfo());

        RouteParameters parameterValues = new RouteParameters();

        if (m.matches()) {
            runtimeBag.getRoute().getPath().parameters.forEach((index, name) -> {
                parameterValues.put(name, resolveRouteParameter(m, index, name, runtimeBag));
            });
        }

        EventDispatcher eventDispatcher = getContainer().get(EventDispatcher.class);

        eventDispatcher.dispatch(KernelEvents.ROUTE_PARAMETERS, new RouteParametersEvent(runtimeBag, parameterValues));

        return parameterValues;
    }

    public Object resolveRouteParameter(Matcher m, Integer index, String name, RuntimeBag runtimeBag)
    {
        String value = m.group(index);
        Object resolvedValue = value;

        for (RouteParameterResolverInterface resolver : routeParameterResolvers) {
            if (resolver.supports(name, value, runtimeBag)) {
                resolvedValue = resolver.resolve(name, value, runtimeBag);
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
