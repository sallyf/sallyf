package com.sallyf.sallyf.Event;

import com.sallyf.sallyf.EventDispatcher.EventInterface;
import com.sallyf.sallyf.Router.ActionInvokerInterface;
import com.sallyf.sallyf.Server.Request;

public class ActionFilterEvent implements EventInterface
{
    private Request request;

    private Object[] parameters;

    private ActionInvokerInterface actionInvoker;

    public ActionFilterEvent(Request request, Object[] parameters, ActionInvokerInterface actionInvoker)
    {

        this.request = request;
        this.parameters = parameters;
        this.actionInvoker = actionInvoker;
    }

    public Request getRequest()
    {
        return request;
    }

    public void setRequest(Request request)
    {
        this.request = request;
    }

    public Object[] getParameters()
    {
        return parameters;
    }

    public void setParameters(Object[] parameters)
    {
        this.parameters = parameters;
    }

    public ActionInvokerInterface getActionInvoker()
    {
        return actionInvoker;
    }

    public void setActionInvoker(ActionInvokerInterface actionInvoker)
    {
        this.actionInvoker = actionInvoker;
    }
}
