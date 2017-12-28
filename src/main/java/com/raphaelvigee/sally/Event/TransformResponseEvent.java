package com.raphaelvigee.sally.Event;

import com.raphaelvigee.sally.EventDispatcher.EventInterface;
import com.raphaelvigee.sally.Server.RuntimeBag;

public class TransformResponseEvent implements EventInterface
{
    private RuntimeBag runtimeBag;

    private Object handlerResponse;

    public TransformResponseEvent(RuntimeBag runtimeBag, Object handlerResponse)
    {
        this.runtimeBag = runtimeBag;
        this.handlerResponse = handlerResponse;
    }

    public RuntimeBag getRuntimeBag()
    {
        return runtimeBag;
    }

    public void setRuntimeBag(RuntimeBag runtimeBag)
    {
        this.runtimeBag = runtimeBag;
    }

    public Object getHandlerResponse()
    {
        return handlerResponse;
    }

    public void setHandlerResponse(Object handlerResponse)
    {
        this.handlerResponse = handlerResponse;
    }
}
