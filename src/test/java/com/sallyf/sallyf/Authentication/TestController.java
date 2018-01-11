package com.sallyf.sallyf.Authentication;

import com.sallyf.sallyf.Annotation.Route;
import com.sallyf.sallyf.Authentication.Annotation.Voter;
import com.sallyf.sallyf.Authentication.DataSource.InMemoryDataSource;
import com.sallyf.sallyf.Authentication.Voter.AuthenticationVoter;
import com.sallyf.sallyf.Controller.BaseController;
import org.eclipse.jetty.server.Request;

public class TestController extends BaseController
{
    @Route(path = "/authenticate")
    public String authenticate(Request request, AuthenticationManager authenticationManager) throws AuthenticationException
    {
        UserInterface u1 = authenticationManager.authenticate(request, "admin", "password");
        UserInterface u2 = authenticationManager.authenticate(request, "admin", "password", InMemoryDataSource.class);

        return u1.getUsername() + u2.getUsername();
    }

    @Route(path = "/user")
    public String user(User user)
    {
        return user.getUsername();
    }

    @Route(path = "/secured")
    @Voter(attribute = AuthenticationVoter.AUTHENTICATED)
    public String secured()
    {
        return "Secured";
    }

    @Route(path = "/secured/{name}")
    @Voter(attribute = NameVoter.ACCESS, parameter = "name")
    public String securedName()
    {
        return "Secured name";
    }
}