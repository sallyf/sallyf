package com.sallyf.sallyf.Event;

import com.sallyf.sallyf.EventDispatcher.EventInterface;
import com.sallyf.sallyf.Server.HTTPSession;

/**
 * Created by raphael on 13/12/17.
 */
public class HTTPSessionEvent implements EventInterface
{
    public HTTPSession session;

    public HTTPSessionEvent(HTTPSession session) {

        this.session = session;
    }
}
