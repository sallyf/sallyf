package com.sallyf.sallyf.Router.Event;

import com.sallyf.sallyf.EventDispatcher.EventInterface;
import com.sallyf.sallyf.Router.ActionInvokerInterface;
import com.sallyf.sallyf.Server.HTTPSession;

public class PreInvokeActionEvent implements EventInterface
{
    private HTTPSession session;

    private Object[] parameters;

    private ActionInvokerInterface actionInvoker;

    public PreInvokeActionEvent(HTTPSession session, Object[] parameters, ActionInvokerInterface actionInvoker)
    {

        this.session = session;
        this.parameters = parameters;
        this.actionInvoker = actionInvoker;
    }

    public ActionInvokerInterface getActionInvoker()
    {
        return actionInvoker;
    }
}
