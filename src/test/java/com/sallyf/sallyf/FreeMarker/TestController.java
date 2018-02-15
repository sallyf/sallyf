package com.sallyf.sallyf.FreeMarker;

import com.sallyf.sallyf.Annotation.Route;
import com.sallyf.sallyf.Controller.BaseController;

import java.util.HashMap;
import java.util.Map;

public class TestController extends BaseController
{
    @Route(path = "/freemarker-response")
    public FreeMarkerResponse freeMarkerResponse()
    {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "world");

        return new FreeMarkerResponse("helloworld.ftl", data);
    }
}
