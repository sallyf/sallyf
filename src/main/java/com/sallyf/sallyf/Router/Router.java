package com.sallyf.sallyf.Router;

import com.sallyf.sallyf.BaseController;
import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ContainerAware;
import com.sallyf.sallyf.Event.RouteParametersEvent;
import com.sallyf.sallyf.EventDispatcher.EventDispatcher;
import com.sallyf.sallyf.Exception.FrameworkException;
import com.sallyf.sallyf.Exception.RouteDuplicateException;
import com.sallyf.sallyf.Exception.UnhandledParameterException;
import com.sallyf.sallyf.KernelEvents;
import com.sallyf.sallyf.Router.ActionParameterResolver.ContainerResolver;
import com.sallyf.sallyf.Router.ActionParameterResolver.RequestResolver;
import com.sallyf.sallyf.Router.ActionParameterResolver.RouteParameterResolver;
import com.sallyf.sallyf.Router.ActionParameterResolver.ServiceResolver;
import com.sallyf.sallyf.Server.Method;
import com.sallyf.sallyf.Server.RuntimeBag;
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

        addActionParameterResolver(new ContainerResolver(container));
        addActionParameterResolver(new RequestResolver());
        addActionParameterResolver(new RouteParameterResolver(container));
        addActionParameterResolver(new ServiceResolver(container));
    }

    private HashMap<String, Route> routes = new HashMap<>();

    private ArrayList<RouteParameterResolverInterface> routeParameterResolvers = new ArrayList<>();

    private ArrayList<ActionParameterResolverInterface> actionParameterResolvers = new ArrayList<>();

    private ArrayList<String> routeSignatures = new ArrayList<>();

    public void addController(Class<? extends BaseController> controllerClass) throws FrameworkException
    {
        EventDispatcher eventDispatcher = getContainer().get(EventDispatcher.class);

        com.sallyf.sallyf.Annotation.Route controllerAnnotation = controllerClass.getAnnotation(com.sallyf.sallyf.Annotation.Route.class);

        String pathPrefix = controllerAnnotation == null ? "" : controllerAnnotation.path();

        String actionNamePrefix = controllerClass.getSimpleName() + ".";
        if (controllerAnnotation != null && !controllerAnnotation.name().isEmpty()) {
            actionNamePrefix = controllerAnnotation.name();
        }

        java.lang.reflect.Method[] methods = controllerClass.getMethods();

        for (java.lang.reflect.Method method : methods) {
            if (method.isAnnotationPresent(com.sallyf.sallyf.Annotation.Route.class)) {
                if (!java.lang.reflect.Modifier.isStatic(method.getModifiers())) {
                    System.err.println("Method `" + method.getName() + "` is not static, ignoring");
                    continue;
                }

                com.sallyf.sallyf.Annotation.Route routeAnnotation = method.getAnnotation(com.sallyf.sallyf.Annotation.Route.class);

                final Class<?>[] parameterTypes = method.getParameterTypes();

                String actionName = method.getName();
                if (!routeAnnotation.name().isEmpty()) {
                    actionName = routeAnnotation.name();
                }

                String fullName = actionNamePrefix + actionName;

                addAction(fullName, routeAnnotation.method(), pathPrefix + routeAnnotation.path(), (runtimeBag) -> {
                    Object[] parameters = resolveActionParameters(parameterTypes, runtimeBag);

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

    public Object[] resolveActionParameters(Class<?>[] parameterTypes, RuntimeBag runtimeBag) throws UnhandledParameterException
    {
        Object[] parameters = new Object[parameterTypes.length];
        int i = 0;
        for (Class<?> parameterType : parameterTypes) {
            parameters[i++] = resolveActionParameter(parameterType, runtimeBag);
        }

        return parameters;
    }

    public Object resolveActionParameter(Class parameterType, RuntimeBag runtimeBag) throws UnhandledParameterException
    {
        for (ActionParameterResolverInterface resolver : actionParameterResolvers) {
            if (resolver.supports(parameterType, runtimeBag)) {
                return resolver.resolve(parameterType, runtimeBag);
            }
        }

        throw new UnhandledParameterException(parameterType);
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

        for (RouteParameterResolverInterface resolver : routeParameterResolvers) {
            if (resolver.supports(name, value, runtimeBag)) {
                return resolver.resolve(name, value, runtimeBag);
            }
        }

        return value;
    }

    public void addRouteParameterResolver(RouteParameterResolverInterface resolver)
    {
        routeParameterResolvers.add(resolver);
    }

    public void addActionParameterResolver(ActionParameterResolverInterface resolver)
    {
        actionParameterResolvers.add(resolver);
    }
}
