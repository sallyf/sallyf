package com.sallyf.sallyf.Server;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ContainerAwareInterface;
import com.sallyf.sallyf.Event.RequestEvent;
import com.sallyf.sallyf.Event.ResponseEvent;
import com.sallyf.sallyf.Event.RouteMatchEvent;
import com.sallyf.sallyf.EventDispatcher.EventDispatcher;
import com.sallyf.sallyf.KernelEvents;
import com.sallyf.sallyf.Router.Route;
import com.sallyf.sallyf.Router.Router;
import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server extends NanoHTTPD implements ContainerAwareInterface
{
    private Container container;

    public Server()
    {
        super(4367);
    }

    @Override
    public void start(int timeout, boolean daemon) throws IOException
    {
        getContainer().get(EventDispatcher.class).register(KernelEvents.PRE_SEND_RESPONSE, responseEvent -> {
            Request request = responseEvent.request;

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();

            System.out.println("[" + dateFormat.format(date) + "] " + request.getMethod() + " \"" + request.getUri() + "\"");
        });

        super.start(timeout, daemon);
    }

    @Override
    public Response serve(IHTTPSession s)
    {
        Request request = Request.create(s);

        com.sallyf.sallyf.Router.Response response = serve(request);

        Response nativeResponse;

        if (null == response) {
            nativeResponse = newFixedLengthResponse(Response.Status.OK, "text/plain", null);
        } else {
            nativeResponse = newFixedLengthResponse(response.getStatus(), response.getMimeType(), response.getContent());
        }

        request.getCookies().unloadQueue(nativeResponse);

        return nativeResponse;
    }

    public com.sallyf.sallyf.Router.Response serve(Request request)
    {
        try {
            EventDispatcher eventDispatcher = getContainer().get(EventDispatcher.class);

            Router router = getContainer().get(Router.class);

            eventDispatcher.dispatch(KernelEvents.PRE_MATCH_ROUTE, new RequestEvent(request));

            Route route = router.match(request);
            request.setRoute(route);

            eventDispatcher.dispatch(KernelEvents.POST_MATCH_ROUTE, new RouteMatchEvent(request));

            if (route == null) {
                return new com.sallyf.sallyf.Router.Response("Not found", Status.NOT_FOUND, "text/plain");
            }

            com.sallyf.sallyf.Router.Response response = route.getHandler().apply(request);

            eventDispatcher.dispatch(KernelEvents.PRE_SEND_RESPONSE, new ResponseEvent(request, response));

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return new com.sallyf.sallyf.Router.Response("Internal Error", Status.INTERNAL_ERROR, "text/plain");
        }
    }

    @Override
    public String getHostname()
    {
        return super.getHostname() == null ? "localhost" : super.getHostname();
    }

    public void setContainer(Container container)
    {
        this.container = container;
    }

    public Container getContainer()
    {
        return container;
    }
}

