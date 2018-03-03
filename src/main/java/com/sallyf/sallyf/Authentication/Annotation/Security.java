package com.sallyf.sallyf.Authentication.Annotation;

import com.sallyf.sallyf.Authentication.DeniedHandler.ForbiddenHandler;
import com.sallyf.sallyf.Authentication.SecurityDeniedHandler;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Repeatable(Securities.class)
public @interface Security
{
    String value();

    Class<? extends SecurityDeniedHandler> handler() default ForbiddenHandler.class;
}
