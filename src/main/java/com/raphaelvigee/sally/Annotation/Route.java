package com.raphaelvigee.sally.Annotation;

import com.raphaelvigee.sally.Routing.Method;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Route
{
    String path();

    Method method() default Method.GET;
}
