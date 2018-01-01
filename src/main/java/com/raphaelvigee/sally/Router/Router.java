package com.raphaelvigee.sally.Router;

import com.raphaelvigee.sally.Container.Container;
import com.raphaelvigee.sally.Container.ContainerAware;
import com.raphaelvigee.sally.Controller.ControllerInterface;
import com.raphaelvigee.sally.Event.RouteParametersEvent;
import com.raphaelvigee.sally.Event.RouteRegisterEvent;
import com.raphaelvigee.sally.EventDispatcher.EventDispatcher;
import com.raphaelvigee.sally.Exception.FrameworkException;
import com.raphaelvigee.sally.Exception.InvalidResponseTypeException;
import com.raphaelvigee.sally.Exception.RouteDuplicateException;
import com.raphaelvigee.sally.Exception.UnhandledParameterException;
import com.raphaelvigee.sally.KernelEvents;
import com.raphaelvigee.sally.Router.ActionParameterResolver.RequestResolver;
import com.raphaelvigee.sally.Router.ActionParameterResolver.RouteParameterResolver;
import com.raphaelvigee.sally.Router.ActionParameterResolver.ServiceResolver;
import com.raphaelvigee.sally.Router.ResponseTransformer.PrimitiveTransformer;
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
    private HashMap<String, Route> routes = new HashMap<>();

    private ArrayList<RouteParameterResolverInterface> routeParameterResolvers = new ArrayList<>();

    private ArrayList<ActionParameterResolverInterface> actionParameterResolvers = new ArrayList<>();

    private ArrayList<ResponseTransformerInterface> responseTransformers = new ArrayList<>();

    private ArrayList<String> routeSignatures = new ArrayList<>();

    public Router(Container container)
    {
        super(container);

        addActionParameterResolver(new RequestResolver());
        addActionParameterResolver(new RouteParameterResolver(container));
        addActionParameterResolver(new ServiceResolver(container));

        addResponseTransformer(new PrimitiveTransformer());
    }

    public <C extends ControllerInterface> C registerController(Class<C> controllerClass) throws FrameworkException
    {
        EventDispatcher eventDispatcher = getContainer().get(EventDispatcher.class);

        C controller = getContainer().add(controllerClass);

        com.raphaelvigee.sally.Annotation.Route controllerAnnotation = controllerClass.getAnnotation(com.raphaelvigee.sally.Annotation.Route.class);

        String pathPrefix = controllerAnnotation == null ? "" : controllerAnnotation.path();

        String actionNamePrefix = controllerClass.getSimpleName() + ".";
        if (controllerAnnotation != null && !controllerAnnotation.name().isEmpty()) {
            actionNamePrefix = controllerAnnotation.name();
        }

        java.lang.reflect.Method[] methods = controllerClass.getMethods();

        for (java.lang.reflect.Method method : methods) {
            if (method.isAnnotationPresent(com.raphaelvigee.sally.Annotation.Route.class)) {
                com.raphaelvigee.sally.Annotation.Route routeAnnotation = method.getAnnotation(com.raphaelvigee.sally.Annotation.Route.class);

                final Class<?>[] parameterTypes = method.getParameterTypes();

                String actionName = method.getName();
                if (!routeAnnotation.name().isEmpty()) {
                    actionName = routeAnnotation.name();
                }

                String fullName = actionNamePrefix + actionName;

                Route route = registerAction(fullName, routeAnnotation.method(), pathPrefix + routeAnnotation.path(), (runtimeBag) -> {
                    Object[] parameters = resolveActionParameters(parameterTypes, runtimeBag);

                    try {
                        return method.invoke(controller, parameters);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                        return null;
                    }
                });

                eventDispatcher.dispatch(KernelEvents.ROUTE_REGISTER, new RouteRegisterEvent(route, controller, method));
            }
        }

        return controller;
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

    public Route registerRoute(String name, Route route) throws RouteDuplicateException
    {
        String signature = route.getMethod() + " " + route.getPath().getPattern();
        if (routeSignatures.contains(signature)) {
            throw new RouteDuplicateException(route);
        }

        route.setName(name);

        routes.put(name, route);
        routeSignatures.add(signature);

        return route;
    }

    public Route registerAction(String name, Method method, String path, ActionWrapperInterface handler) throws RouteDuplicateException
    {
        return registerRoute(name, new Route(name, method, path, handler));
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

    public Response transformResponse(RuntimeBag runtimeBag, Object response) throws InvalidResponseTypeException
    {
        if (response instanceof Response) {
            return (Response) response;
        }

        for (ResponseTransformerInterface transformer : responseTransformers) {
            if (transformer.supports(runtimeBag, response)) {
                return transformer.transform(runtimeBag, response);
            }
        }

        throw new InvalidResponseTypeException(response);
    }

    public void addRouteParameterResolver(RouteParameterResolverInterface resolver)
    {
        routeParameterResolvers.add(resolver);
    }

    public void addActionParameterResolver(ActionParameterResolverInterface resolver)
    {
        actionParameterResolvers.add(resolver);
    }

    public void addResponseTransformer(ResponseTransformerInterface transformer)
    {
        responseTransformers.add(transformer);
    }
}
