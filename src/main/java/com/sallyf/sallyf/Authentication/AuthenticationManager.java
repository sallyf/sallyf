package com.sallyf.sallyf.Authentication;

import com.sallyf.sallyf.AccessDecisionManager.AccessDecisionManager;
import com.sallyf.sallyf.Authentication.Annotation.Security;
import com.sallyf.sallyf.Authentication.Exception.AuthenticationException;
import com.sallyf.sallyf.Authentication.Voter.AuthenticationVoter;
import com.sallyf.sallyf.Container.ConfigurationInterface;
import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ServiceInterface;
import com.sallyf.sallyf.Container.ServiceDefinition;
import com.sallyf.sallyf.EventDispatcher.EventDispatcher;
import com.sallyf.sallyf.Exception.HttpException;
import com.sallyf.sallyf.ExpressionLanguage.ExpressionLanguage;
import com.sallyf.sallyf.KernelEvents;
import com.sallyf.sallyf.Router.ActionParameterResolver.UserInterfaceResolver;
import com.sallyf.sallyf.Router.Route;
import com.sallyf.sallyf.Router.Router;
import com.sallyf.sallyf.Server.RuntimeBag;
import com.sallyf.sallyf.Server.Status;
import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class AuthenticationManager implements ServiceInterface
{
    private ArrayList<UserDataSourceInterface> dataSources;

    private HashMap<Route, Security> securedRoutes = new HashMap<>();

    private Router router;

    private EventDispatcher eventDispatcher;

    private final ExpressionLanguage expressionLanguage;

    public AuthenticationManager(Configuration configuration, Router router, EventDispatcher eventDispatcher, ExpressionLanguage expressionLanguage)
    {
        this.router = router;
        this.eventDispatcher = eventDispatcher;
        this.expressionLanguage = expressionLanguage;

        dataSources = configuration.getDataSources();
    }

    public void initialize(Container container)
    {
        container
                .add(new ServiceDefinition<>(AuthenticationVoter.class))
                .addTag(AccessDecisionManager.TAG_VOTER);

        router.addActionParameterResolver(new UserInterfaceResolver(this));

        eventDispatcher.register(KernelEvents.ROUTE_REGISTER, (et, routeRegisterEvent) -> {
            Method method = routeRegisterEvent.getMethod();

            Security annotation = method.getAnnotation(Security.class);

            if (null != annotation) {
                securedRoutes.put(routeRegisterEvent.getRoute(), annotation);
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
            Security annotation = securedRoutes.get(route);

            return this.expressionLanguage.evaluate(annotation.value(), runtimeBag);
        }

        return true;
    }

    public UserInterface authenticate(Request request, String username, String password)
    {
        return authenticate(request, username, password, null);
    }

    public UserInterface authenticate(Request request, String username, String password, Class<? extends UserDataSourceInterface> dataSourceClass)
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

    public UserDataSourceInterface getDataSource(Class<? extends UserDataSourceInterface> dataSourceClass)
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
