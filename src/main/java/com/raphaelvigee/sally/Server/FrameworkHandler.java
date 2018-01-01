package com.raphaelvigee.sally.Server;

import com.raphaelvigee.sally.Container.Container;
import com.raphaelvigee.sally.Container.ContainerAwareInterface;
import com.raphaelvigee.sally.Event.RequestEvent;
import com.raphaelvigee.sally.Event.ResponseEvent;
import com.raphaelvigee.sally.Event.RouteMatchEvent;
import com.raphaelvigee.sally.Event.TransformResponseEvent;
import com.raphaelvigee.sally.EventDispatcher.EventDispatcher;
import com.raphaelvigee.sally.KernelEvents;
import com.raphaelvigee.sally.Router.RedirectResponse;
import com.raphaelvigee.sally.Router.Response;
import com.raphaelvigee.sally.Router.Route;
import com.raphaelvigee.sally.Router.Router;
import org.eclipse.jetty.http.HttpCookie;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

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
                       HttpServletResponse r)
    {

        org.eclipse.jetty.server.Response servletResponse = (org.eclipse.jetty.server.Response) r;

        applyResponse(servletResponse, handle(servletRequest, servletResponse));

        baseRequest.setHandled(true);
    }

    private com.raphaelvigee.sally.Router.Response handle(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
    {
        Request request = (Request) servletRequest;

        try {

            EventDispatcher eventDispatcher = getContainer().get(EventDispatcher.class);

            Router router = getContainer().get(Router.class);

            eventDispatcher.dispatch(KernelEvents.REQUEST, new RequestEvent(request));

            Route route = router.match(request);

            if (route == null) {
                return new com.raphaelvigee.sally.Router.Response("Not Found", Status.NOT_FOUND, "text/plain");
            }

            route = (Route) route.clone();

            RuntimeBag runtimeBag = new RuntimeBag(request, route);

            eventDispatcher.dispatch(KernelEvents.POST_MATCH_ROUTE, new RouteMatchEvent(runtimeBag));

            Object handlerResponse = route.getHandler().apply(runtimeBag);

            eventDispatcher.dispatch(KernelEvents.PRE_TRANSFORM_RESPONSE, new TransformResponseEvent(runtimeBag, handlerResponse));

            com.raphaelvigee.sally.Router.Response response = router.transformResponse(runtimeBag, handlerResponse);

            eventDispatcher.dispatch(KernelEvents.PRE_SEND_RESPONSE, new ResponseEvent(runtimeBag, response));

            return response;
        } catch (Exception e) {
            e.printStackTrace();

            return new com.raphaelvigee.sally.Router.Response("Internal Error", Status.INTERNAL_ERROR, "text/plain");
        }
    }

    public void applyResponse(org.eclipse.jetty.server.Response servletResponse, Response response)
    {
        if (response instanceof RedirectResponse) {
            RedirectResponse redirectResponse = (RedirectResponse) response;

            try {
                servletResponse.sendRedirect(redirectResponse.getStatus().getRequestStatus(), redirectResponse.getTargetUrl());
            } catch (IOException e) {
                e.printStackTrace();
                Response error = new Response("Internal Error", Status.INTERNAL_ERROR, "text/plain");

                applyResponse(servletResponse, error);
                return;
            }
        }

        for (Map.Entry<String, ArrayList<String>> entry : response.getHeaders().entrySet()) {
            String name = entry.getKey();
            ArrayList<String> headers = entry.getValue();
            for (String values : headers) {
                servletResponse.addHeader(name, values);
            }
        }

        for (HttpCookie cookie : response.getCookies()) {
            servletResponse.addCookie(cookie);
        }

        servletResponse.setContentType(response.getMimeType());
        servletResponse.setStatus(response.getStatus().getRequestStatus());

        try {
            servletResponse.getWriter().print(response.getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Container getContainer()
    {
        return container;
    }
}
