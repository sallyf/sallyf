package com.sallyf.sallyf.Annotation;

import com.sallyf.sallyf.Server.Method;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Route
{
    String path();

    Method[] methods() default {Method.GET};

    String name() default "";

    Requirement[] requirements() default {};
}
