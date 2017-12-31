package com.raphaelvigee.sally.Server;

import com.raphaelvigee.sally.Annotation.Route;
import com.raphaelvigee.sally.Controller.BaseController;
import com.raphaelvigee.sally.Router.Response;

public class TestController extends BaseController
{
    @Route(path = "/test1")
    public Response test1() {
        Response r = new Response("OK");

        r.addHeader("test1", "hello1");

        return r;
    }
}
