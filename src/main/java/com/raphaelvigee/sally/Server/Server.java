package com.raphaelvigee.sally.Server;

import com.raphaelvigee.sally.Container.Container;
import com.raphaelvigee.sally.Container.ContainerAwareInterface;
import com.raphaelvigee.sally.EventDispatcher.EventDispatcher;
import com.raphaelvigee.sally.Router.*;
import com.raphaelvigee.sally.Server.Event.HTTPSessionEvent;
import com.raphaelvigee.sally.Server.Event.ResponseEvent;
import com.raphaelvigee.sally.Server.Event.RouteMatchEvent;
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

            com.raphaelvigee.sally.Server.HTTPSession session = com.raphaelvigee.sally.Server.HTTPSession.create(s);

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

            com.raphaelvigee.sally.Router.Response response;

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

