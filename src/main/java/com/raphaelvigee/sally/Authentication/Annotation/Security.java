package com.raphaelvigee.sally.Authentication.Annotation;

import com.raphaelvigee.sally.Authentication.SecurityValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Security
{
    Class<? extends SecurityValidator>[] value();
}
