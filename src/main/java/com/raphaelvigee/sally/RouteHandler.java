package com.raphaelvigee.sally;

public interface RouteHandler<I, O>
{
    public O call(I input);
}