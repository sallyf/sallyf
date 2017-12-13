package com.sallyf.sallyf.Router;

import com.sallyf.sallyf.EventDispatcher.EventType;
import com.sallyf.sallyf.Router.Event.PreInvokeActionEvent;

public class Events
{
    public static final EventType<PreInvokeActionEvent> PRE_INVOKE_ACTION = new EventType<>("router.pre_invoke_action");
}
