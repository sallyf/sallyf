package com.sallyf.sallyf.Authentication;

import com.sallyf.sallyf.Annotation.Route;
import com.sallyf.sallyf.Authentication.Annotation.Security;
import com.sallyf.sallyf.Controller.BaseController;

@Route(path = "/test2")
@Security("false")
public class Test2Controller extends BaseController
{
    @Route(path = "/test")
    public String test()
    {
        return "";
    }
}
