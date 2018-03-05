package com.sallyf.sallyf.Authentication;

import com.sallyf.sallyf.Annotation.Route;
import com.sallyf.sallyf.Authentication.Annotation.Security;
import com.sallyf.sallyf.Authentication.DataSource.InMemoryDataSource;
import com.sallyf.sallyf.Controller.BaseController;
import org.eclipse.jetty.server.Request;

public class TestController extends BaseController
{
    @Route(path = "/authenticate")
    public String authenticate(Request request, AuthenticationManager authenticationManager)
    {
        UserInterface u1 = authenticationManager.authenticate("admin", "password");
        UserInterface u2 = authenticationManager.authenticate("admin", "password", InMemoryDataSource.class);

        return u1.getUsername() + u2.getUsername();
    }

    @Route(path = "/user")
    public String user(User user)
    {
        return user.getUsername();
    }

    @Route(path = "/secured")
    @Security("is_granted('authenticated')")
    public String secured()
    {
        return "Secured";
    }

    @Route(path = "/secured/{name}")
    @Security("is_granted('access', name)")
    public String securedName()
    {
        return "Secured name";
    }

    @Route(path = "/secured/authenticated/{name}")
    @Security("is_granted('access', name) && is_granted('authenticated')")
    public String securedAuthenticatedName()
    {
        return "Secured name authenticated";
    }
}