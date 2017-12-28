package com.sallyf.sallyf.Server;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ContainerAwareInterface;
import com.sallyf.sallyf.Event.RequestEvent;
import com.sallyf.sallyf.Event.ResponseEvent;
import com.sallyf.sallyf.Event.RouteMatchEvent;
import com.sallyf.sallyf.Event.TransformResponseEvent;
import com.sallyf.sallyf.EventDispatcher.EventDispatcher;
import com.sallyf.sallyf.KernelEvents;
import com.sallyf.sallyf.Router.Route;
import com.sallyf.sallyf.Router.Router;
import com.sallyf.sallyf.Util.Jetty;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FrameworkHandler extends AbstractHandler implements ContainerAwareInterface
{
    private Container container;

    public FrameworkHandler(Container container)
    {
        this.container = container;
    }

    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest servletRequest,
                       HttpServletResponse servletResponse)
    {

        Jetty.applyResponse(servletResponse, handle(servletRequest, servletResponse));

        baseRequest.setHandled(true);
    }

    private com.sallyf.sallyf.Router.Response handle(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
    {
        Request request = (Request) servletRequest;

        try {

            EventDispatcher eventDispatcher = getContainer().get(EventDispatcher.class);

            Router router = getContainer().get(Router.class);

            eventDispatcher.dispatch(KernelEvents.PRE_MATCH_ROUTE, new RequestEvent(request));

            Route route = router.match(request);

            if (route == null) {
                return new com.sallyf.sallyf.Router.Response("Not Found", Status.NOT_FOUND, "text/plain");
            }

            route = (Route) route.clone();

            RuntimeBag runtimeBag = new RuntimeBag(request, route);

            eventDispatcher.dispatch(KernelEvents.POST_MATCH_ROUTE, new RouteMatchEvent(runtimeBag));

            Object handlerResponse = route.getHandler().apply(runtimeBag);

            eventDispatcher.dispatch(KernelEvents.PRE_TRANSFORM_RESPONSE, new TransformResponseEvent(runtimeBag, handlerResponse));

            com.sallyf.sallyf.Router.Response response = router.transformResponse(runtimeBag, handlerResponse);

            eventDispatcher.dispatch(KernelEvents.PRE_SEND_RESPONSE, new ResponseEvent(runtimeBag, response));

            return response;
        } catch (Exception e) {
            e.printStackTrace();

            return new com.sallyf.sallyf.Router.Response("Internal Error", Status.INTERNAL_ERROR, "text/plain");
        }
    }

    @Override
    public Container getContainer()
    {
        return container;
    }
}
