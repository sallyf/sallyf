package com.sallyf.sallyf.Server;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ContainerAwareInterface;
import com.sallyf.sallyf.EventDispatcher.EventDispatcher;
import com.sallyf.sallyf.KernelEvents;
import com.sallyf.sallyf.Router.Route;
import com.sallyf.sallyf.Router.Router;
import com.sallyf.sallyf.Event.HTTPSessionEvent;
import com.sallyf.sallyf.Event.ResponseEvent;
import com.sallyf.sallyf.Event.RouteMatchEvent;
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
        container.get(EventDispatcher.class).register(KernelEvents.PRE_SEND_RESPONSE, responseEvent -> {
            com.sallyf.sallyf.Server.HTTPSession session = responseEvent.session;

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();

            System.out.println("[" + dateFormat.format(date) + "] " + session.getMethod() + " \"" + session.getUri() + "\"");
        });

        super.start(timeout, daemon);
    }

    @Override
    public Response serve(IHTTPSession s)
    {
        try {
            EventDispatcher eventDispatcher = container.get(EventDispatcher.class);

            com.sallyf.sallyf.Server.HTTPSession session = com.sallyf.sallyf.Server.HTTPSession.create(s);

            Router router = container.get(Router.class);

            eventDispatcher.dispatch(KernelEvents.PRE_MATCH_ROUTE, new HTTPSessionEvent(session));

            Route route = router.match(session);
            session.setRoute(route);

            eventDispatcher.dispatch(KernelEvents.POST_MATCH_ROUTE, new RouteMatchEvent(session));

            if (route == null) {
                return newFixedLengthResponse(Status.NOT_FOUND, "text/plain", "Not found");
            }

            com.sallyf.sallyf.Router.Response response = route.getHandler().apply(session);

            eventDispatcher.dispatch(KernelEvents.PRE_SEND_RESPONSE, new ResponseEvent(session, response));

            return newFixedLengthResponse(response.getStatus(), response.getMimeType(), response.getContent());
        } catch (Exception e) {
            e.printStackTrace();
            return newFixedLengthResponse(Status.INTERNAL_ERROR, "text/plain", "Internal Error");
        }
    }

    @Override
    public String getHostname()
    {
        return super.getHostname() == null ? "localhost" : super.getHostname();
    }

    @Override
    public void setContainer(Container container)
    {
        this.container = container;
    }
}

