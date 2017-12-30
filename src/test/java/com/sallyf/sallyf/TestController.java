package com.sallyf.sallyf;

import com.sallyf.sallyf.Annotation.Route;
import com.sallyf.sallyf.Controller.BaseController;
import com.sallyf.sallyf.Exception.UnableToGenerateURLException;
import com.sallyf.sallyf.Router.Response;
import com.sallyf.sallyf.Router.RouteParameters;

@Route(path = "/prefixed", name = "test_")
public class TestController extends BaseController
{
    public Response hello1()
    {
        return new Response("hello");
    }

    @Route(path = "/hello", name = "hello_named")
    public Response hello4()
    {
        return new Response("hello");
    }

    @Route(path = "/redirect", name = "redirect")
    public Response redirect() throws UnableToGenerateURLException
    {
        return redirectToRoute("test_hello_named");
    }

    @Route(path = "/hello/{name}")
    public Response hello5(RouteParameters parameters)
    {
        return new Response("hello, " + parameters.get("name"));
    }

    @Route(path = "/transform/{name}")
    public Object toTransform(RouteParameters parameters)
    {
        return "hello, " + parameters.get("name");
    }
}
