package com.sallyf.sallyf.Authentication.Annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Voter
{
    String attribute();

    String parameter() default "";
}
