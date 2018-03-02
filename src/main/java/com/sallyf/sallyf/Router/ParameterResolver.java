package com.sallyf.sallyf.Router;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Repeatable(ParameterResolvers.class)
public @interface ParameterResolver
{
    String name();

    Class<? extends RouteParameterResolverInterface> type();
}
