package com.sallyf.sallyf.Authentication.Annotation;

import com.sallyf.sallyf.Authentication.Voter.VoterInterface;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Security
{
    Class<? extends VoterInterface>[] value();
}
