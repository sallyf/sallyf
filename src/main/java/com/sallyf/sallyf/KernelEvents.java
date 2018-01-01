package com.sallyf.sallyf;

import com.sallyf.sallyf.Event.*;
import com.sallyf.sallyf.EventDispatcher.EventType;

public class KernelEvents
{
    //  Dispatched before matching a `Route` to the `HTTPSession`
    public static final EventType<RequestEvent> PRE_MATCH_ROUTE = new EventType<>("kernel.pre_match_route");

    // Dispatched after matching a `Route` to the `HTTPSession`
    public static final EventType<RouteMatchEvent> POST_MATCH_ROUTE = new EventType<>("kernel.post_match_route");

    // Dispatched before sending the `Response`
    public static final EventType<ResponseEvent> PRE_SEND_RESPONSE = new EventType<>("kernel.pre_send_response");

    // Dispatched before transforming the handler response to a `Response`
    public static final EventType<TransformResponseEvent> PRE_TRANSFORM_RESPONSE = new EventType<>("kernel.pre_transform_response");

    // Dispatched after binding the URL parameters
    public static final EventType<RouteParametersEvent> ROUTE_PARAMETERS = new EventType<>("kernel.route_parameters");

    // Dispatched after registering a route from a controller
    public static final EventType<RouteRegisterEvent> ROUTE_REGISTER = new EventType<>("kernel.route_register");
}
