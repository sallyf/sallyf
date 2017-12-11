package com.sallyf.sallyf;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ContainerAwareInterface;
import com.sallyf.sallyf.Routing.*;
import fi.iki.elonen.NanoHTTPD;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class YServer extends NanoHTTPD implements ContainerAwareInterface
{
    private Container container;

    public YServer()
    {
        super(4367);
    }

    @Override
    public Response serve(IHTTPSession s)
    {
        com.sallyf.sallyf.Routing.HTTPSession session = com.sallyf.sallyf.Routing.HTTPSession.create(s);

        Routing routing = container.get(Routing.class);

        Route match = routing.match(session);

        if (match == null) {
            return newFixedLengthResponse(Status.NOT_FOUND, "text/plain", "Not found");
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        System.out.println("["+dateFormat.format(date)+"] "+session.getMethod()+" \""+session.getUri()+"\"");

        com.sallyf.sallyf.Routing.Response response;

        try {
            response = match.getHandler().apply(container, session, match);
        } catch (Exception e) {
            e.printStackTrace();
            return newFixedLengthResponse(Status.INTERNAL_ERROR, "text/plain", "Internal Error");
        }

        return newFixedLengthResponse(response.getStatus(), response.getMimeType(), response.getContent());
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

