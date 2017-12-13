package com.raphaelvigee.sally.Server;

import com.raphaelvigee.sally.EventDispatcher.EventType;
import com.raphaelvigee.sally.Server.Event.HTTPSessionEvent;
import com.raphaelvigee.sally.Server.Event.ResponseEvent;
import com.raphaelvigee.sally.Server.Event.RouteMatchEvent;

public class Events
{
    public static final EventType<HTTPSessionEvent> PRE_MATCH = new EventType<>("server.pre_match");

    public static final EventType<ResponseEvent> PRE_SEND_RESPONSE = new EventType<>("server.pre_send_response");

    public static final EventType<RouteMatchEvent> POST_MATCH = new EventType<>("server.post_match");
}
