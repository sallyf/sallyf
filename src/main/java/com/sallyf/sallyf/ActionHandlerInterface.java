package com.sallyf.sallyf;

@FunctionalInterface
public interface ActionHandlerInterface<A, B, C, R>
{
    R apply(A var1, B var2, C var3);
}
