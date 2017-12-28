package com.raphaelvigee.sally.Controller;

import com.raphaelvigee.sally.Container.Container;
import com.raphaelvigee.sally.Exception.UnableToGenerateURLException;
import com.raphaelvigee.sally.Router.RedirectResponse;
import com.raphaelvigee.sally.Router.Response;
import com.raphaelvigee.sally.Router.URLGenerator;
import com.raphaelvigee.sally.Server.Status;

import java.util.HashMap;

abstract public class BaseController implements ControllerInterface
{
    private Container container;

    public void setContainer(Container container)
    {
        this.container = container;
    }

    @Override
    public Container getContainer()
    {
        return container;
    }

    public Response redirect(String url)
    {
        return new RedirectResponse(url, Status.REDIRECT);
    }

    public Response redirectToRoute(String actionName) throws UnableToGenerateURLException
    {
        return redirectToRoute(actionName, new HashMap<>());
    }

    public Response redirectToRoute(String actionName, HashMap<String, String> parameters) throws UnableToGenerateURLException
    {
        return redirect(getContainer().get(URLGenerator.class).url(actionName, parameters));
    }
}
