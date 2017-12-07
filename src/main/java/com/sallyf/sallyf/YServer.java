package com.sallyf.sallyf;

import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;

public class YServer extends NanoHTTPD implements ContainerAwareInterface
{
    private Container container;

    public YServer()
    {
        super(4367);

        try {
            start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Listening on http://" + getHostname() + ":" + getListeningPort());
    }

    @Override
    public Response serve(IHTTPSession s)
    {
        com.sallyf.sallyf.HTTPSession session = com.sallyf.sallyf.HTTPSession.create(s);

        Routing routing = container.get(Routing.class);

        Route match = routing.match(session);

        if (match == null) {
            return newFixedLengthResponse("404");
        }

        return newFixedLengthResponse(match.getHandler().apply(session, match).content);
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

