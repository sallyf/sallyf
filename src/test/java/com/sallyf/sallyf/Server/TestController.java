package com.sallyf.sallyf.Server;

import com.sallyf.sallyf.Annotation.Route;
import com.sallyf.sallyf.Controller.BaseController;
import com.sallyf.sallyf.Router.Response;

public class TestController extends BaseController
{
    @Route(path = "/test1")
    public Response test1() {
        Response r = new Response("OK");

        r.addHeader("test1", "hello1");

        r.addCookie("cookie1", "hello1");

        return r;
    }
}
