package com.sallyf.sallyf.Controller;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Router.RedirectResponse;
import com.sallyf.sallyf.Router.Response;
import com.sallyf.sallyf.Router.URLGenerator;
import com.sallyf.sallyf.Server.Status;

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
        return new RedirectResponse(url, Status.MOVED_TEMPORARILY);
    }

    public Response redirectToRoute(String actionName)
    {
        return redirectToRoute(actionName, new HashMap<>());
    }

    public Response redirectToRoute(String actionName, HashMap<String, String> parameters)
    {
        return redirect(getContainer().get(URLGenerator.class).url(actionName, parameters));
    }
}
