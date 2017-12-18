package com.sallyf.sallyf.Server;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ContainerAwareInterface;
import com.sallyf.sallyf.EventDispatcher.EventDispatcher;
import com.sallyf.sallyf.KernelEvents;
import com.sallyf.sallyf.Router.Router;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.SessionIdManager;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.session.DefaultSessionIdManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;


public class FrameworkServer extends Server implements ContainerAwareInterface
{
    private static final Logger LOG = Log.getLogger(FrameworkServer.class);

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

    @Override
    protected void doStart() throws Exception
    {
        super.doStart();

        getContainer().get(EventDispatcher.class).register(KernelEvents.PRE_SEND_RESPONSE, responseEvent -> {
            Request request = responseEvent.getRequest();

            LOG.info(request.getMethod() + " \"" + request.getPathInfo() + "\"");
        });
    }

    public Container getContainer()
    {
        return container;
    }
}

