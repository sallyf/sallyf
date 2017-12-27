# Events

All framework events can be found in the [`KernelEvents`](/src/main/java/Framework/KernelEvents.java) file. 

This table presents the lifecycle of a request. The events are presented in the order in which they are dispatched.

| Event | Description |
| --- | --- |
| `PRE_MATCH_ROUTE` | Dispatched before matching a `Route` to the `HTTPSession` |
| `POST_MATCH_ROUTE` | Dispatched after matching a `Route` to the `HTTPSession` |
| `ROUTE_PARAMETERS` | Dispatched after binding the URL parameters |
| `PRE_SEND_RESPONSE` | Dispatched before sending the `Response` |
