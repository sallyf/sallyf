package com.raphaelvigee.sally.Router.Event;

import com.raphaelvigee.sally.EventDispatcher.EventInterface;
import com.raphaelvigee.sally.Router.ActionInvokerInterface;
import com.raphaelvigee.sally.Server.HTTPSession;

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
