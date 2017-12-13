package com.sallyf.sallyf.Server;

import com.sallyf.sallyf.EventDispatcher.EventType;
import com.sallyf.sallyf.Server.Event.HTTPSessionEvent;
import com.sallyf.sallyf.Server.Event.ResponseEvent;
import com.sallyf.sallyf.Server.Event.RouteMatchEvent;

public class Events
{
    public static final EventType<HTTPSessionEvent> PRE_MATCH = new EventType<>("server.pre_match");

    public static final EventType<ResponseEvent> PRE_SEND_RESPONSE = new EventType<>("server.pre_send_response");

    public static final EventType<RouteMatchEvent> POST_MATCH = new EventType<>("server.post_match");
}
