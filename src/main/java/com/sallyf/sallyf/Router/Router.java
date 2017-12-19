package com.sallyf.sallyf.Router;

import com.sallyf.sallyf.BaseController;
import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ContainerAware;
import com.sallyf.sallyf.Event.ActionFilterEvent;
import com.sallyf.sallyf.Event.RouteParametersEvent;
import com.sallyf.sallyf.EventDispatcher.EventDispatcher;
import com.sallyf.sallyf.Exception.FrameworkException;
import com.sallyf.sallyf.Exception.RouteDuplicateException;
import com.sallyf.sallyf.Exception.UnhandledParameterException;
import com.sallyf.sallyf.KernelEvents;
import com.sallyf.sallyf.Server.Method;
import com.sallyf.sallyf.Server.RuntimeBag;
import org.eclipse.jetty.server.Request;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Router extends ContainerAware
{
    public Router(Container container)
    {
        super(container);
    }

    private ArrayList<Route> routes = new ArrayList<>();

    private ArrayList<RouteParameterResolverInterface> routeParameterResolvers = new ArrayList<>();

    private ArrayList<String> routeSignatures = new ArrayList<>();

    public void addController(Class<? extends BaseController> controllerClass) throws FrameworkException
    {
        EventDispatcher eventDispatcher = getContainer().get(EventDispatcher.class);

        com.sallyf.sallyf.Annotation.Route controllerAnnotation = controllerClass.getAnnotation(com.sallyf.sallyf.Annotation.Route.class);

        String pathPrefix = controllerAnnotation == null ? "" : controllerAnnotation.path();

        java.lang.reflect.Method[] methods = controllerClass.getMethods();

        for (java.lang.reflect.Method method : methods) {
            if (method.isAnnotationPresent(com.sallyf.sallyf.Annotation.Route.class)) {
                if (!java.lang.reflect.Modifier.isStatic(method.getModifiers())) {
                    System.err.println("Method `" + method.getName() + "` is not static, ignoring");
                    continue;
                }

                com.sallyf.sallyf.Annotation.Route routeAnnotation = method.getAnnotation(com.sallyf.sallyf.Annotation.Route.class);

                final Class<?>[] parameterTypes = method.getParameterTypes();

                final ActionInvokerInterface actionInvoker = (parameters) -> {
                    try {
                        return (Response) method.invoke(null, parameters);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                        return null;
                    }
                };

                addAction(routeAnnotation.method(), pathPrefix + routeAnnotation.path(), (requestBag) -> {
                    Object[] parameters = getActionParameters(parameterTypes, requestBag);

                    ActionFilterEvent actionFilterEvent = new ActionFilterEvent(requestBag, parameters, actionInvoker);

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

    public Route match(Request request)
    {
        for (Route route : routes) {
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
