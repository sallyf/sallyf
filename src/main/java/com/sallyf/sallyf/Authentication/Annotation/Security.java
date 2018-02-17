package com.sallyf.sallyf.Authentication.Annotation;

import com.sallyf.sallyf.Authentication.DeniedHandler.ForbiddenHandler;
import com.sallyf.sallyf.Authentication.SecurityDeniedHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Security
{
    String value();

    Class<? extends SecurityDeniedHandler> handler() default ForbiddenHandler.class;
}
