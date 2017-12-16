package com.raphaelvigee.sally.Event;

import com.raphaelvigee.sally.EventDispatcher.EventInterface;
import com.raphaelvigee.sally.Router.ActionInvokerInterface;
import com.raphaelvigee.sally.Server.Request;

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
