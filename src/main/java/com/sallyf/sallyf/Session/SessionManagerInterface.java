package com.sallyf.sallyf.Session;

import com.sallyf.sallyf.Container.ContainerAwareInterface;
import com.sallyf.sallyf.Server.Request;

public interface SessionManagerInterface<S> extends ContainerAwareInterface
{
    S getSession(Request request);

    S getSession(String sessionId);

    S createSession(Request request);
}
