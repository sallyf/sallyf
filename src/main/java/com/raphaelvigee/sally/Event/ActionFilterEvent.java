package com.raphaelvigee.sally.Event;

import com.raphaelvigee.sally.EventDispatcher.EventInterface;
import com.raphaelvigee.sally.Router.ActionInvokerInterface;
import com.raphaelvigee.sally.Server.RuntimeBag;

public class ActionFilterEvent implements EventInterface
{
    private Object[] parameters;

    private ActionInvokerInterface actionInvoker;

    private RuntimeBag runtimeBag;

    public ActionFilterEvent(RuntimeBag runtimeBag, Object[] parameters, ActionInvokerInterface actionInvoker)
    {
        this.runtimeBag = runtimeBag;
        this.parameters = parameters;
        this.actionInvoker = actionInvoker;
    }

    public RuntimeBag getRuntimeBag()
    {
        return runtimeBag;
    }

    public void setRuntimeBag(RuntimeBag runtimeBag)
    {
        this.runtimeBag = runtimeBag;
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
