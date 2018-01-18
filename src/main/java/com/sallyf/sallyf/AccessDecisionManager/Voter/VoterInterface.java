package com.sallyf.sallyf.AccessDecisionManager.Voter;

import com.sallyf.sallyf.Container.ServiceInterface;
import com.sallyf.sallyf.Server.RuntimeBag;

public interface VoterInterface<S> extends ServiceInterface
{
    boolean supports(String attribute, Object subject, RuntimeBag runtimeBag);

    boolean vote(String attribute, S subject, RuntimeBag runtimeBag);
}
