package com.sallyf.sallyf.Server;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ContainerAwareInterface;
import com.sallyf.sallyf.EventDispatcher.EventDispatcher;
import com.sallyf.sallyf.Router.*;
import com.sallyf.sallyf.Server.Event.HTTPSessionEvent;
import com.sallyf.sallyf.Server.Event.ResponseEvent;
import com.sallyf.sallyf.Server.Event.RouteMatchEvent;
import fi.iki.elonen.NanoHTTPD;

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
    public Response serve(IHTTPSession s)
    {
        try {
            EventDispatcher eventDispatcher = container.get(EventDispatcher.class);

            com.sallyf.sallyf.Server.HTTPSession session = com.sallyf.sallyf.Server.HTTPSession.create(s);

            Router router = container.get(Router.class);

            eventDispatcher.dispatch(Events.PRE_MATCH, new HTTPSessionEvent(session));

            Route match = router.match(session);

            eventDispatcher.dispatch(Events.POST_MATCH, new RouteMatchEvent(session, match));

            if (match == null) {
                return newFixedLengthResponse(Status.NOT_FOUND, "text/plain", "Not found");
            }

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();

            System.out.println("["+dateFormat.format(date)+"] "+session.getMethod()+" \""+session.getUri()+"\"");

            com.sallyf.sallyf.Router.Response response;

            response = match.getHandler().apply(container, session, match);

            eventDispatcher.dispatch(Events.PRE_SEND_RESPONSE, new ResponseEvent(session, response));

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

