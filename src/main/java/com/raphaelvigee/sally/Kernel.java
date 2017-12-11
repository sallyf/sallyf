package com.raphaelvigee.sally;

import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;
import java.util.ArrayList;

public class Kernel
{
    private Container container;

    Kernel(Container container)
    {
        this.container = container;
    }

    public static Kernel newInstance()
    {
        Container container = new Container();

        container.add(YServer.class);
        container.add(Routing.class);

        return new Kernel(container);
    }

    public Container getContainer()
    {
        return container;
    }

    public void start()
    {
        YServer server = container.get(YServer.class);
        Routing routing = container.get(Routing.class);
        try {
            server.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Route> routes = routing.getRoutes();

        System.out.println(routes.size()+" routes registered:");
        for (Route route : routes) {
            System.out.println(route.toString());
        }
        System.out.println();

        System.out.println("Listening on http://" + server.getHostname() + ":" + server.getListeningPort());
        System.out.println();
    }
}
