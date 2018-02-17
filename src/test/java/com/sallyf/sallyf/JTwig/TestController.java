package com.sallyf.sallyf.JTwig;

import com.sallyf.sallyf.Annotation.Route;
import com.sallyf.sallyf.Controller.BaseController;

import java.util.HashMap;
import java.util.Map;

public class TestController extends BaseController
{
    @Route(path = "/jtwig-response")
    public JTwigResponse jtwigResponse()
    {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "world");

        return new JTwigResponse("helloworld.twig", data);
    }
}
