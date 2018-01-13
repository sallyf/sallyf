package com.sallyf.sallyf.Router;

import com.sallyf.sallyf.Container.ContainerAwareInterface;
import com.sallyf.sallyf.Exception.UnableToGenerateURLException;
import com.sallyf.sallyf.Server.FrameworkServer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class URLGenerator implements ContainerAwareInterface
{
    private final FrameworkServer server;

    private final Router router;

    public URLGenerator(FrameworkServer server, Router router)
    {
        this.server = server;
        this.router = router;
    }

    public String url(String actionName, HashMap<String, String> parameters)
    {
        return server.getRootURL() + path(actionName, parameters);
    }

    public String url(String actionName)
    {
        return url(actionName, new HashMap<>());
    }

    public String path(String actionName, HashMap<String, String> parameters)
    {
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

    public String path(String actionName)
    {
        return path(actionName, new HashMap<>());
    }
}
