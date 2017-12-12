package com.raphaelvigee.sally;

import com.raphaelvigee.sally.Annotation.Route;
import com.raphaelvigee.sally.Routing.Response;

@Route(path = "/prefixed")
public class TestController extends BaseController
{
    public static Response hello1()
    {
        return new Response("hello");
    }

    public Response hello2()
    {
        return new Response("hello");
    }

    @Route(path = "/hello")
    public Response hello3()
    {
        return new Response("hello");
    }

    @Route(path = "/hello")
    public static Response hello4()
    {
        return new Response("hello");
    }
}
