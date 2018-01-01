package com.raphaelvigee.sally;

import com.raphaelvigee.sally.Container.Container;
import com.raphaelvigee.sally.EventDispatcher.EventDispatcher;
import com.raphaelvigee.sally.Exception.FrameworkException;
import com.raphaelvigee.sally.Router.Route;
import com.raphaelvigee.sally.Router.Router;
import com.raphaelvigee.sally.Router.URLGenerator;
import com.raphaelvigee.sally.Server.FrameworkServer;

import java.util.HashMap;

public class Kernel
{
    private Container container;

    Kernel(Container container)
    {
        this.container = container;
    }

    public static Kernel newInstance() throws FrameworkException
    {
        Container container = new Container();

        container.add(FrameworkServer.class);
        container.add(Router.class);
        container.add(URLGenerator.class);
        container.add(EventDispatcher.class);

        return new Kernel(container);
    }

    public Container getContainer()
    {
        return container;
    }

    public void boot()
    {
        getContainer().get(EventDispatcher.class).dispatch(KernelEvents.BOOT);

        FrameworkServer server = container.get(FrameworkServer.class);
        Router router = container.get(Router.class);
        try {
            server.start();
            //server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        HashMap<String, Route> routes = router.getRoutes();

        System.out.println(routes.size() + " routes registered:");
        for (Route route : routes.values()) {
            System.out.println(route.getName() + " -> " + route.toString());
        }
        System.out.println();

        System.out.println("Listening on " + server.getRootURL());
        System.out.println();
    }

    public void stop()
    {
        FrameworkServer server = container.get(FrameworkServer.class);

        try {
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
