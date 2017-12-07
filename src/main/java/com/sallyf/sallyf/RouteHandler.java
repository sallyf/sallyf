package com.sallyf.sallyf;

public interface RouteHandler<I, O>
{
    public O call(I input);
}