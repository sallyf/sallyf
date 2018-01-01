package com.raphaelvigee.sally.Authentication;

import com.raphaelvigee.sally.Annotation.Route;
import com.raphaelvigee.sally.Authentication.Annotation.Security;
import com.raphaelvigee.sally.Authentication.DataSource.InMemoryDataSource;
import com.raphaelvigee.sally.Authentication.Voter.LoggedIn;
import com.raphaelvigee.sally.Controller.BaseController;
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
    @Security({LoggedIn.class})
    public String secured()
    {
        return "Secured";
    }
}