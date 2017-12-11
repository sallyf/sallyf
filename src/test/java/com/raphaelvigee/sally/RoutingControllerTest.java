package com.raphaelvigee.sally;

import com.raphaelvigee.sally.Annotation.Route;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

class Controller1 extends BaseController
{
    public static Response hello1()
    {
        return new Response("hello");
    }

    public Response hello2()
    {
        return new Response("hello");
    }
}

class Controller2 extends BaseController
{
    @Route(path = "hello")
    public Response hello()
    {
        return new Response("hello");
    }
}

class Controller3 extends BaseController
{
    @Route(path = "hello")
    public static Response hello()
    {
        return new Response("hello");
    }
}

public class RoutingControllerTest
{
    @Test
    public void addEmptyTest() throws Exception
    {
        Routing routing = new Routing();

        routing.addController(Controller1.class);

        assertEquals(0, routing.getRoutes().size());
    }

    @Test
    public void addInvalidTest() throws Exception
    {
        Routing routing = new Routing();

        routing.addController(Controller2.class);

        assertEquals(0, routing.getRoutes().size());
    }

    @Test
    public void addTest() throws Exception
    {
        Routing routing = new Routing();

        routing.addController(Controller3.class);

        ArrayList<com.raphaelvigee.sally.Route> routes = routing.getRoutes();

        assertEquals(1, routes.size());

        Response response = routes.get(0).getHandler().apply(null, null, null);

        assertEquals("hello", response.content);
    }
}
