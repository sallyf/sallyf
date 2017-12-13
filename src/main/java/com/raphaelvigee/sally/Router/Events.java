package com.raphaelvigee.sally.Router;

import com.raphaelvigee.sally.EventDispatcher.EventType;
import com.raphaelvigee.sally.Router.Event.PreInvokeActionEvent;

public class Events
{
    public static final EventType<PreInvokeActionEvent> PRE_INVOKE_ACTION = new EventType<>("router.pre_invoke_action");
}
