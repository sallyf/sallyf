package com.raphaelvigee.sally.Router;

import com.raphaelvigee.sally.Container.Container;
import com.raphaelvigee.sally.Container.ContainerAwareInterface;
import com.raphaelvigee.sally.Exception.UnableToGenerateURLException;
import com.raphaelvigee.sally.Server.FrameworkServer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class URLGenerator implements ContainerAwareInterface
{
    private Container container;

    public URLGenerator(Container container)
    {
        this.container = container;
    }

    public String url(String actionName, HashMap<String, String> parameters) throws UnableToGenerateURLException
    {
        FrameworkServer server = getContainer().get(FrameworkServer.class);

        return server.getRootURL() + path(actionName, parameters);
    }

    public String url(String actionName) throws UnableToGenerateURLException
    {
        return url(actionName, new HashMap<>());
    }

    public String path(String actionName, HashMap<String, String> parameters) throws UnableToGenerateURLException
    {
        Router router = getContainer().get(Router.class);
        FrameworkServer server = getContainer().get(FrameworkServer.class);

        Route route = router.getRoutes().get(actionName);

        if (route == null) {
            throw new UnableToGenerateURLException(actionName, parameters);
        }

        Collection<String> routeParameterNames = route.getPath().getParameters().values();
        Collection<String> parameterNames = parameters.keySet();

        if (!parameterNames.containsAll(routeParameterNames)) {
            throw new UnableToGenerateURLException(actionName, parameters);
        }

        String path = route.getPath().getDeclaration();

        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            path = path.replace(String.format("{%s}", key), value);
        }

        return path;
    }

    public String path(String actionName) throws UnableToGenerateURLException
    {
        return path(actionName, new HashMap<>());
    }

    @Override
    public Container getContainer()
    {
        return container;
    }
}
