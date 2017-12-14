package com.sallyf.sallyf;

import com.sallyf.sallyf.Event.ActionFilterEvent;
import com.sallyf.sallyf.Event.HTTPSessionEvent;
import com.sallyf.sallyf.Event.ResponseEvent;
import com.sallyf.sallyf.Event.RouteMatchEvent;
import com.sallyf.sallyf.EventDispatcher.EventType;

public class KernelEvents
{
    public static final EventType<HTTPSessionEvent> PRE_MATCH_ROUTE = new EventType<>("kernel.pre_match_route");

    public static final EventType<RouteMatchEvent> POST_MATCH_ROUTE = new EventType<>("kernel.post_match_route");

    public static final EventType<ResponseEvent> PRE_SEND_RESPONSE = new EventType<>("kernel.pre_send_response");

    public static final EventType<ActionFilterEvent> ACTION_FILTER = new EventType<>("kernel.pre_invoke_action");
}
