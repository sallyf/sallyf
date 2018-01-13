package com.sallyf.sallyf.AccessDecisionManager;

import com.sallyf.sallyf.Server.RuntimeBag;

@FunctionalInterface
public interface PredicateIsGrantedHandler<O>
{
    boolean isGranted(RuntimeBag runtimeBag, String attribute, O subject, DecisionStrategy strategy);

    default boolean isGranted(RuntimeBag runtimeBag, String attribute, O subject)
    {
        return isGranted(runtimeBag, attribute, subject, DecisionStrategy.AFFIRMATIVE);
    }

    default boolean isGranted(RuntimeBag runtimeBag, String attribute)
    {
        return isGranted(runtimeBag, attribute, null);
    }
}
