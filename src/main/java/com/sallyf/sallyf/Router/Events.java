package com.sallyf.sallyf.Router;

import com.sallyf.sallyf.EventDispatcher.EventType;
import com.sallyf.sallyf.Router.Event.ActionFilterEvent;

public class Events
{
    public static final EventType<ActionFilterEvent> ACTION_FILTER = new EventType<>("router.pre_invoke_action");
}
