package com.sallyf.sallyf.Authentication;

import com.sallyf.sallyf.Container.Container;

@FunctionalInterface
public interface SecurityDeniedHandler
{
    Object apply(Container container);
}
