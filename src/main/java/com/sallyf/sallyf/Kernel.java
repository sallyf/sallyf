package com.sallyf.sallyf;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.Exception.ServiceInstantiationException;
import com.sallyf.sallyf.Container.ServiceDefinition;
import com.sallyf.sallyf.EventDispatcher.EventDispatcher;
import com.sallyf.sallyf.Router.Route;
import com.sallyf.sallyf.Router.Router;
import com.sallyf.sallyf.Router.URLGenerator;
import com.sallyf.sallyf.Server.FrameworkServer;

import java.util.HashMap;

public class Kernel
{
    private Container container;

    public Kernel(Container container) throws ServiceInstantiationException
    {
        this.container = container;

        container.addAll(getDefaultServices());
    }

    public static Kernel newInstance() throws ServiceInstantiationException
    {
        return new Kernel(new Container());
    }

    private ServiceDefinition[] getDefaultServices()
    {
        return new ServiceDefinition[]{
                new ServiceDefinition<>(EventDispatcher.class),
                new ServiceDefinition<>(FrameworkServer.class),
                new ServiceDefinition<>(Router.class),
                new ServiceDefinition<>(URLGenerator.class),
        };
    }

    public Container getContainer()
    {
        return container;
    }

    public void boot() throws ServiceInstantiationException
    {
        container.instantiate();

        EventDispatcher eventDispatcher = container.get(EventDispatcher.class);

        eventDispatcher.dispatch(KernelEvents.BOOTED);

        eventDispatcher.register(KernelEvents.STARTED, (eventType, eventInterface) -> {
            FrameworkServer server = container.get(FrameworkServer.class);
            Router router = container.get(Router.class);

            HashMap<String, Route> routes = router.getRoutes();

            System.out.println(routes.size() + " routes registered:");
            for (Route route : routes.values()) {
                System.out.println(route.getName() + " -> " + route.toString());
            }
            System.out.println();

            System.out.println("Listening on " + server.getRootURL());
            System.out.println();
        });
    }

    public void start()
    {
        EventDispatcher eventDispatcher = container.get(EventDispatcher.class);

        eventDispatcher.dispatch(KernelEvents.START);

        FrameworkServer server = this.container.get(FrameworkServer.class);

        try {
            server.start();
            //server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        eventDispatcher.dispatch(KernelEvents.STARTED);
    }

    public void stop()
    {
        FrameworkServer server = container.get(FrameworkServer.class);

        if (null != server) {
            try {
                server.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
