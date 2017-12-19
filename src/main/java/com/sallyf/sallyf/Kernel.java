package com.sallyf.sallyf;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.EventDispatcher.EventDispatcher;
import com.sallyf.sallyf.Exception.FrameworkException;
import com.sallyf.sallyf.Router.Route;
import com.sallyf.sallyf.Router.Router;
import com.sallyf.sallyf.Server.FrameworkServer;

import java.util.ArrayList;

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
        container.add(EventDispatcher.class);

        return new Kernel(container);
    }

    public Container getContainer()
    {
        return container;
    }

    public void start()
    {
        FrameworkServer server = container.get(FrameworkServer.class);
        Router router = container.get(Router.class);
        try {
            server.start();
            //server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<Route> routes = router.getRoutes();

        System.out.println(routes.size() + " routes registered:");
        for (Route route : routes) {
            System.out.println(route.toString());
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
