package com.sallyf.sallyf;

import com.sallyf.sallyf.Annotation.Requirement;
import com.sallyf.sallyf.Annotation.Route;
import com.sallyf.sallyf.Controller.BaseController;
import com.sallyf.sallyf.Exception.UnableToGenerateURLException;
import com.sallyf.sallyf.Router.Response;
import com.sallyf.sallyf.Router.RouteParameters;

import java.util.ArrayList;
import java.util.List;

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
        return redirectToRoute("test_redirect_target");
    }

    @Route(path = "/redirect-target", name = "redirect_target")
    public String redirectTarget()
    {
        return "Redirect target";
    }

    @Route(path = "/hello/{name}")
    public Response hello5(RouteParameters parameters)
    {
        return new Response("hello, " + parameters.get("name") + " " + parameters.get("___", "fallback"));
    }

    @Route(path = "/hello/slashed/{path}", requirements = {
            @Requirement(name = "path", requirement = "(.+)")
    })
    public Object helloSlashed(RouteParameters parameters)
    {
        return parameters.get("path");
    }

    @Route(path = "/invalidresponse")
    public List invalidResponse()
    {
        return new ArrayList();
    }

    @Route(path = "/resolve/{name}")
    public Object toTransform(RouteParameters parameters)
    {
        return "hello, " + parameters.get("name");
    }
}
