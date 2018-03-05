package com.sallyf.sallyf.Router;

import com.sallyf.sallyf.Annotation.Requirement;
import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ServiceInterface;
import com.sallyf.sallyf.Controller.ControllerInterface;
import com.sallyf.sallyf.Event.RouteParametersEvent;
import com.sallyf.sallyf.Event.RouteRegisterEvent;
import com.sallyf.sallyf.EventDispatcher.EventDispatcher;
import com.sallyf.sallyf.Exception.ControllerInstantiationException;
import com.sallyf.sallyf.Exception.FrameworkException;
import com.sallyf.sallyf.Exception.InvalidResponseTypeException;
import com.sallyf.sallyf.Exception.UnhandledParameterException;
import com.sallyf.sallyf.KernelEvents;
import com.sallyf.sallyf.Router.ActionParameterResolver.RequestResolver;
import com.sallyf.sallyf.Router.ActionParameterResolver.RouteParameterResolver;
import com.sallyf.sallyf.Router.ActionParameterResolver.RuntimeBagResolver;
import com.sallyf.sallyf.Router.ActionParameterResolver.ServiceResolver;
import com.sallyf.sallyf.Router.ResponseTransformer.HttpExceptionTransformer;
import com.sallyf.sallyf.Router.ResponseTransformer.PrimitiveTransformer;
import com.sallyf.sallyf.Server.RuntimeBag;
import com.sallyf.sallyf.Server.RuntimeBagContext;
import com.sallyf.sallyf.Utils.ClassUtils;
import org.eclipse.jetty.server.Request;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Router implements ServiceInterface
{
    private final Container container;

    private final EventDispatcher eventDispatcher;

    private HashMap<String, Route> routes = new HashMap<>();

    private Map<Class, ControllerInterface> controllers = new HashMap<>();

    private ArrayList<ActionParameterResolverInterface> actionParameterResolvers = new ArrayList<>();

    private ArrayList<ResponseTransformerInterface> responseTransformers = new ArrayList<>();

    public Router(Container container, EventDispatcher eventDispatcher)
    {
        this.container = container;
        this.eventDispatcher = eventDispatcher;
    }

    public void initialize(Container container)
    {
        addActionParameterResolver(new RequestResolver());
        addActionParameterResolver(new RuntimeBagResolver());
        addActionParameterResolver(new RouteParameterResolver(container));
        addActionParameterResolver(new ServiceResolver(container));

        addResponseTransformer(new PrimitiveTransformer());
        addResponseTransformer(new HttpExceptionTransformer());
    }

    public <C extends ControllerInterface> C registerController(Class<C> controllerClass)
    {
        C controller = instantiateController(controllerClass);

        controllers.put(controllerClass, controller);

        com.sallyf.sallyf.Annotation.Route controllerAnnotation = controllerClass.getAnnotation(com.sallyf.sallyf.Annotation.Route.class);

        String pathPrefix = controllerAnnotation == null ? "" : controllerAnnotation.path();

        String actionNamePrefix = controllerClass.getSimpleName() + ".";
        if (controllerAnnotation != null && !controllerAnnotation.name().isEmpty()) {
            actionNamePrefix = controllerAnnotation.name();
        }

        ParameterResolver[] controllerResolvers = controllerClass.getAnnotationsByType(ParameterResolver.class);

        java.lang.reflect.Method[] methods = controllerClass.getMethods();

        for (java.lang.reflect.Method method : methods) {
            if (method.isAnnotationPresent(com.sallyf.sallyf.Annotation.Route.class)) {
                com.sallyf.sallyf.Annotation.Route routeAnnotation = method.getAnnotation(com.sallyf.sallyf.Annotation.Route.class);

                com.sallyf.sallyf.Annotation.Route[] annotations;
                if (controllerAnnotation == null) {
                    annotations = new com.sallyf.sallyf.Annotation.Route[]{routeAnnotation};
                } else {
                    annotations = new com.sallyf.sallyf.Annotation.Route[]{controllerAnnotation, routeAnnotation};
                }

                final Parameter[] parameters = method.getParameters();

                String actionName = method.getName();
                if (!routeAnnotation.name().isEmpty()) {
                    actionName = routeAnnotation.name();
                }

                ParameterResolver[] methodResolvers = method.getAnnotationsByType(ParameterResolver.class);

                ParameterResolver[] resolverAnnotations = Stream.of(controllerResolvers, methodResolvers)
                        .flatMap(Stream::of)
                        .toArray(ParameterResolver[]::new);

                HashMap<String, RouteParameterResolverInterface> resolvers = new HashMap<>();

                for (ParameterResolver r : resolverAnnotations) {
                    RouteParameterResolverInterface resolver = container.get(r.type());
                    resolvers.put(r.name(), resolver);
                }

                String fullName = actionNamePrefix + actionName;
                String fullPath = pathPrefix + routeAnnotation.path();

                ActionWrapperInterface handler = () -> {
                    Object[] resolvedParameters = resolveActionParameters(parameters);

                    try {
                        return method.invoke(controller, resolvedParameters);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                        return null;
                    }
                };

                Route route = new Route(fullName, routeAnnotation.methods(), fullPath, handler);
                route.setRouteParameterResolvers(resolvers);

                Path path = route.getPath();

                for (com.sallyf.sallyf.Annotation.Route annotation : annotations) {
                    for (Requirement requirement : annotation.requirements()) {
                        path.getRequirements().put(requirement.name(), requirement.requirement());
                    }
                }

                registerRoute(fullName, route);

                eventDispatcher.dispatch(KernelEvents.ROUTE_REGISTER, new RouteRegisterEvent(route, controller, method));
            }
        }

        return controller;
    }

    public Object[] resolveActionParameters(Parameter[] parameters)
    {
        Object[] outParameters = new Object[parameters.length];
        int i = 0;
        for (Parameter parameter : parameters) {
            outParameters[i++] = resolveActionParameter(parameter);
        }

        return outParameters;
    }

    public Object resolveActionParameter(Parameter parameter)
    {
        for (ActionParameterResolverInterface resolver : actionParameterResolvers) {
            if (resolver.supports(parameter)) {
                return resolver.resolve(parameter);
            }
        }

        throw new UnhandledParameterException(parameter);
    }

    public Route registerRoute(String name, Route route)
    {
        route.getPath().computePattern();

        route.setName(name);

        routes.put(name, route);

        return route;
    }

    private <T extends ControllerInterface> T instantiateController(Class<T> controllerClass)
    {
        T controller = ClassUtils.newInstance(controllerClass, ControllerInstantiationException::new);

        controller.setContainer(container);

        return controller;
    }

    public HashMap<String, Route> getRoutes()
    {
        return routes;
    }

    public Route match(Request request)
    {
        for (Route route : routes.values()) {
            if (Stream.of(route.getMethods()).map(Enum::toString).anyMatch(request.getMethod()::equalsIgnoreCase)) {
                Pattern r = Pattern.compile(route.getPath().getPattern());

                Matcher m = r.matcher(request.getPathInfo());

                if (m.matches()) {
                    return route;
                }
            }
        }

        return null;
    }

    public RouteParameters getRouteParameters()
    {
        RuntimeBag runtimeBag = RuntimeBagContext.get();

        Pattern r = Pattern.compile(runtimeBag.getRoute().getPath().getPattern());

        Matcher m = r.matcher(runtimeBag.getRequest().getPathInfo());

        RouteParameters parameterValues = new RouteParameters();

        if (m.matches()) {
            runtimeBag.getRoute().getPath().getParameters().forEach((index, name) -> {
                parameterValues.put(name, resolveRouteParameter(m, index, name));
            });
        }

        eventDispatcher.dispatch(KernelEvents.ROUTE_PARAMETERS, new RouteParametersEvent(runtimeBag, parameterValues));

        return parameterValues;
    }

    public Object resolveRouteParameter(Matcher m, Integer index, String name)
    {
        RuntimeBag runtimeBag = RuntimeBagContext.get();

        String value = m.group(index);

        Map<String, RouteParameterResolverInterface> routeParameterResolvers = runtimeBag.getRoute().getRouteParameterResolvers();

        if (routeParameterResolvers.containsKey(name)) {
            RouteParameterResolverInterface resolver = routeParameterResolvers.get(name);

            return resolver.resolve(name, value);
        }

        return value;
    }

    public Response transformResponse(Object response)
    {
        while (true) {
            boolean transformed = false;
            for (ResponseTransformerInterface transformer : responseTransformers) {
                if (transformer.supports(response)) {
                    try {
                        response = transformer.transform(response);
                    } catch (Exception e) {
                        throw new FrameworkException(e);
                    }
                    transformed = true;
                }
            }

            if (response instanceof Response) {
                return (Response) response;
            }

            if (!transformed) {
                throw new InvalidResponseTypeException(response);
            }
        }
    }

    public Map<Class, ControllerInterface> getControllers()
    {
        return controllers;
    }

    public void addActionParameterResolver(ActionParameterResolverInterface resolver)
    {
        actionParameterResolvers.add(resolver);
    }

    public ArrayList<ActionParameterResolverInterface> getActionParameterResolvers()
    {
        return actionParameterResolvers;
    }

    public void addResponseTransformer(ResponseTransformerInterface transformer)
    {
        responseTransformers.add(transformer);
    }

    public ArrayList<ResponseTransformerInterface> getResponseTransformers()
    {
        return responseTransformers;
    }
}
