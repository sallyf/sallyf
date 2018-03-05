package com.sallyf.sallyf.AccessDecisionManager.Voter;

import com.sallyf.sallyf.Container.ServiceInterface;

public interface VoterInterface<S> extends ServiceInterface
{
    boolean supports(String attribute, Object subject);

    boolean vote(String attribute, S subject);
}
