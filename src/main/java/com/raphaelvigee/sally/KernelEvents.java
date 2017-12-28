package com.raphaelvigee.sally;

import com.raphaelvigee.sally.Event.*;
import com.raphaelvigee.sally.EventDispatcher.EventType;

public class KernelEvents
{
    public static final EventType<RequestEvent> PRE_MATCH_ROUTE = new EventType<>("kernel.pre_match_route");

    public static final EventType<RouteMatchEvent> POST_MATCH_ROUTE = new EventType<>("kernel.post_match_route");

    public static final EventType<ResponseEvent> PRE_SEND_RESPONSE = new EventType<>("kernel.pre_send_response");

    public static final EventType<TransformResponseEvent> PRE_TRANSFORM_RESPONSE = new EventType<>("kernel.pre_transform_response");

    public static final EventType<RouteParametersEvent> ROUTE_PARAMETERS = new EventType<>("kernel.route_parameters");
}
