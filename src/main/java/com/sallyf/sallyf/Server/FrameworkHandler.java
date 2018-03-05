package com.sallyf.sallyf.Server;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Event.RequestEvent;
import com.sallyf.sallyf.Event.ResponseEvent;
import com.sallyf.sallyf.Event.RouteMatchEvent;
import com.sallyf.sallyf.Event.TransformResponseEvent;
import com.sallyf.sallyf.EventDispatcher.EventDispatcher;
import com.sallyf.sallyf.Exception.HttpException;
import com.sallyf.sallyf.KernelEvents;
import com.sallyf.sallyf.Router.RedirectResponse;
import com.sallyf.sallyf.Router.Response;
import com.sallyf.sallyf.Router.Route;
import com.sallyf.sallyf.Router.Router;
import org.eclipse.jetty.http.HttpCookie;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class FrameworkHandler extends AbstractHandler
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

    private com.sallyf.sallyf.Router.Response handle(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
    {
        Request request = (Request) servletRequest;

        EventDispatcher eventDispatcher = container.get(EventDispatcher.class);
        Router router = container.get(Router.class);

        try {
            RuntimeBag runtimeBag = new RuntimeBag();
            ThreadAttributes.set("_runtime_bag", runtimeBag);

            Object handlerResponse;

            try {
                eventDispatcher.dispatch(KernelEvents.REQUEST, new RequestEvent(request));
                runtimeBag.setRequest(request);

                Route route = router.match(request);

                if (route == null) {
                    throw new HttpException(Status.NOT_FOUND);
                }

                route = (Route) route.clone();
                runtimeBag.setRoute(route);

                eventDispatcher.dispatch(KernelEvents.POST_MATCH_ROUTE, new RouteMatchEvent(runtimeBag));

                handlerResponse = route.getHandler().apply();

                eventDispatcher.dispatch(KernelEvents.PRE_TRANSFORM_RESPONSE, new TransformResponseEvent(runtimeBag, handlerResponse));
            } catch (HttpException httpException) {
                handlerResponse = httpException;

                eventDispatcher.dispatch(KernelEvents.PRE_TRANSFORM_RESPONSE, new TransformResponseEvent(runtimeBag, handlerResponse));
            }

            com.sallyf.sallyf.Router.Response response = router.transformResponse(handlerResponse);

            eventDispatcher.dispatch(KernelEvents.PRE_SEND_RESPONSE, new ResponseEvent(runtimeBag, response));

            return response;
        } catch (Exception e) {
            e.printStackTrace();

            return new com.sallyf.sallyf.Router.Response("Internal Error", Status.INTERNAL_ERROR, "text/plain");
        }
    }

    public void applyResponse(org.eclipse.jetty.server.Response servletResponse, Response response)
    {
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

        if (response instanceof RedirectResponse) {
            RedirectResponse redirectResponse = (RedirectResponse) response;

            try {
                servletResponse.sendRedirect(redirectResponse.getStatus().getRequestStatus(), redirectResponse.getTargetUrl());
            } catch (IOException e) {
                e.printStackTrace();
                Response error = new Response("Internal Error", Status.INTERNAL_ERROR, "text/plain");

                applyResponse(servletResponse, error);
            }

            return;
        }

        servletResponse.setContentType(response.getMimeType());
        servletResponse.setStatus(response.getStatus().getRequestStatus());

        try {
            servletResponse.getWriter().print(response.getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
