package com.raphaelvigee.sally.Router;

import com.raphaelvigee.sally.EventDispatcher.EventType;
import com.raphaelvigee.sally.Router.Event.ActionFilterEvent;

public class Events
{
    public static final EventType<ActionFilterEvent> ACTION_FILTER = new EventType<>("router.pre_invoke_action");
}
