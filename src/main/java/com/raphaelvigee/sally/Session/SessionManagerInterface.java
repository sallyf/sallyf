package com.raphaelvigee.sally.Session;

import com.raphaelvigee.sally.Container.ContainerAwareInterface;
import com.raphaelvigee.sally.Server.Request;

public interface SessionManagerInterface<S> extends ContainerAwareInterface
{
    S getSession(Request request);

    S getSession(String sessionId);

    S createSession(Request request);
}
