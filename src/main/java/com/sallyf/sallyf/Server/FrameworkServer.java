package com.sallyf.sallyf.Server;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ContainerAwareInterface;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.SessionIdManager;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.session.DefaultSessionIdManager;
import org.eclipse.jetty.server.session.SessionHandler;


public class FrameworkServer extends Server implements ContainerAwareInterface
{
    private Container container;

    public FrameworkServer(Container container)
    {
        super(4367);

        this.container = container;

        // Specify the Session ID Manager
        SessionIdManager sessionIdManager = new DefaultSessionIdManager(this);
        this.setSessionIdManager(sessionIdManager);

        // Sessions are bound to a context.
        ContextHandler context = new ContextHandler("/");
        this.setHandler(context);

        // Create the SessionHandler (wrapper) to handle the sessions
        SessionHandler sessions = new SessionHandler();
        context.setHandler(sessions);

        sessions.setHandler(new FrameworkHandler(getContainer()));
    }

    public Container getContainer()
    {
        return container;
    }
}

