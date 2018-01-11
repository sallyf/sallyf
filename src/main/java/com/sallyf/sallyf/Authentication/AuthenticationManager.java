package com.sallyf.sallyf.Authentication;

import com.sallyf.sallyf.Authentication.Annotation.Voter;
import com.sallyf.sallyf.Authentication.Voter.AuthenticationVoter;
import com.sallyf.sallyf.Authentication.Voter.VoterInterface;
import com.sallyf.sallyf.Container.*;
import com.sallyf.sallyf.Container.Exception.ServiceInstantiationException;
import com.sallyf.sallyf.EventDispatcher.EventDispatcher;
import com.sallyf.sallyf.Exception.HttpException;
import com.sallyf.sallyf.KernelEvents;
import com.sallyf.sallyf.Router.ActionParameterResolver.UserInterfaceResolver;
import com.sallyf.sallyf.Router.Route;
import com.sallyf.sallyf.Router.RouteParameters;
import com.sallyf.sallyf.Router.Router;
import com.sallyf.sallyf.Server.RuntimeBag;
import com.sallyf.sallyf.Server.Status;
import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class AuthenticationManager implements ContainerAwareInterface
{
    private static final Tag<VoterInterface> TAG_VOTER = new Tag<>("authentication.voter");

    private ArrayList<UserDataSourceInterface> dataSources;

    private HashMap<Route, Voter[]> securedRoutes = new HashMap<>();

    private Container container;

    private Router router;

    private EventDispatcher eventDispatcher;

    public AuthenticationManager(Configuration configuration, Container container, Router router, EventDispatcher eventDispatcher) throws ServiceInstantiationException
    {
        this.container = container;
        this.router = router;
        this.eventDispatcher = eventDispatcher;

        dataSources = configuration.getDataSources();

        container
                .add(new ServiceDefinition<>(AuthenticationVoter.class))
                .addTag(TAG_VOTER);
    }

    public void initialize()
    {
        router.addActionParameterResolver(new UserInterfaceResolver(this));

        eventDispatcher.register(KernelEvents.ROUTE_REGISTER, (et, routeRegisterEvent) -> {
            Method method = routeRegisterEvent.getMethod();

            Voter[] annotations = method.getAnnotationsByType(Voter.class);

            if (annotations.length > 0) {
                securedRoutes.put(routeRegisterEvent.getRoute(), annotations);
            }
        });

        eventDispatcher.register(KernelEvents.POST_MATCH_ROUTE, (et1, routeMatchEvent) -> {
            Route route = routeMatchEvent.getRuntimeBag().getRoute();
            if (!vote(route, routeMatchEvent.getRuntimeBag())) {
                route.setHandler(rb -> {
                    throw new HttpException(Status.FORBIDDEN);
                });
            }
        });
    }

    private boolean vote(Route route, RuntimeBag runtimeBag)
    {
        if (securedRoutes.containsKey(route)) {
            Voter[] annotations = securedRoutes.get(route);
            for (Voter annotation : annotations) {
                for (VoterInterface voter : container.getByTag(TAG_VOTER)) {
                    Object subject = null;

                    if (!annotation.parameter().equals("")) {
                        RouteParameters routeParameters = router.getRouteParameters(runtimeBag);

                        subject = routeParameters.get(annotation.parameter());
                    }

                    if (voter.supports(annotation.attribute(), subject, runtimeBag)) {
                        if (!voter.vote(annotation.attribute(), subject, runtimeBag)) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    public UserInterface authenticate(Request request, String username, String password) throws AuthenticationException
    {
        return authenticate(request, username, password, null);
    }

    public UserInterface authenticate(Request request, String username, String password, Class<? extends UserDataSourceInterface> dataSourceClass) throws AuthenticationException
    {
        if (dataSources.size() == 0) {
            throw new AuthenticationException("No datasource provided");
        }

        UserDataSourceInterface dataSource;

        if (dataSourceClass == null) {
            if (dataSources.size() == 1) {
                dataSource = dataSources.get(0);
            } else {
                throw new AuthenticationException("Ambiguous datasource");
            }
        } else {
            dataSource = getDataSource(dataSourceClass);
        }

        UserInterface user = dataSource.getUser(username, password);

        request.getSession(true).setAttribute("user", user);

        return user;
    }

    public ArrayList<UserDataSourceInterface> getDataSources()
    {
        return dataSources;
    }

    public UserDataSourceInterface getDataSource(Class<? extends UserDataSourceInterface> dataSourceClass) throws AuthenticationException
    {
        for (UserDataSourceInterface dataSource : dataSources) {
            if (dataSource.getClass().equals(dataSourceClass)) {
                return dataSource;
            }
        }

        throw new AuthenticationException("No datasource found for class: " + dataSourceClass);
    }

    public UserInterface getUser(RuntimeBag runtimeBag)
    {
        Request request = runtimeBag.getRequest();

        HttpSession session = request.getSession();

        if (null == session) {
            return null;
        }

        return (UserInterface) session.getAttribute("user");
    }

    public static Class<? extends ConfigurationInterface> getDefaultConfigurationClass()
    {
        return Configuration.class;
    }
}
