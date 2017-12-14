package com.raphaelvigee.sally.Event;

import com.raphaelvigee.sally.EventDispatcher.EventInterface;
import com.raphaelvigee.sally.Server.HTTPSession;

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
