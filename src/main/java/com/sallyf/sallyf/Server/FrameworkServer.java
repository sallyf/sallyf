package com.sallyf.sallyf.Server;

import com.sallyf.sallyf.Container.ConfigurationInterface;
import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ServiceInterface;
import com.sallyf.sallyf.EventDispatcher.EventDispatcher;
import com.sallyf.sallyf.KernelEvents;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SessionIdManager;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.session.DefaultSessionIdManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

public class FrameworkServer extends Server implements ServiceInterface
{
    private static final Logger LOG = Log.getLogger(FrameworkServer.class);

    private Container container;

    public FrameworkServer(Container container, Configuration configuration)
    {
        super(configuration.getPort());

        setStopTimeout(Long.MAX_VALUE);

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

        getContainer().get(EventDispatcher.class).register(KernelEvents.PRE_SEND_RESPONSE, (eventType, responseEvent) -> {
            Request request = responseEvent.getRuntimeBag().getRequest();

            LOG.info(request.getMethod() + " \"" + request.getPathInfo() + "\"");
        });
    }

    public Container getContainer()
    {
        return container;
    }

    public String getRootURL()
    {
        return getRootURL(null);
    }

    public String getRootURL(RuntimeBag runtimeBag)
    {
        ServerConnector connector = (ServerConnector) getConnectors()[0];

        String host = "localhost";
        String protocol = "http";
        int port = connector.getPort();

        if (connector.getName() != null && !connector.getName().isEmpty()) {
            host = connector.getName();
        }

        try {
            InetAddress addr = InetAddress.getLocalHost();
            if (!addr.getHostName().isEmpty()) {
                host = addr.getHostName();
            }
        } catch (UnknownHostException ignored) {
        }

        if (runtimeBag != null) {
            try {
                URL url = new URL(runtimeBag.getRequest().getRequestURL().toString());

                host = url.getHost();
                port = url.getPort();
                protocol = url.getProtocol();
            } catch (MalformedURLException ignored) {
            }
        }

        return protocol + "://" + host + ":" + port;
    }

    public static Class<? extends ConfigurationInterface> getDefaultConfigurationClass()
    {
        return Configuration.class;
    }
}

