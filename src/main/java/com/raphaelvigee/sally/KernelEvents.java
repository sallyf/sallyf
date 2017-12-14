package com.raphaelvigee.sally;

import com.raphaelvigee.sally.Event.ActionFilterEvent;
import com.raphaelvigee.sally.Event.HTTPSessionEvent;
import com.raphaelvigee.sally.Event.ResponseEvent;
import com.raphaelvigee.sally.Event.RouteMatchEvent;
import com.raphaelvigee.sally.EventDispatcher.EventType;

public class KernelEvents
{
    public static final EventType<HTTPSessionEvent> PRE_MATCH_ROUTE = new EventType<>("kernel.pre_match_route");

    public static final EventType<RouteMatchEvent> POST_MATCH_ROUTE = new EventType<>("kernel.post_match_route");

    public static final EventType<ResponseEvent> PRE_SEND_RESPONSE = new EventType<>("kernel.pre_send_response");

    public static final EventType<ActionFilterEvent> ACTION_FILTER = new EventType<>("kernel.pre_invoke_action");
}
