package com.sallyf.sallyf.Authentication.Voter;

import com.sallyf.sallyf.Container.ContainerAwareInterface;
import com.sallyf.sallyf.Server.RuntimeBag;

public interface VoterInterface<S> extends ContainerAwareInterface
{
    boolean supports(String attribute, Object subject, RuntimeBag runtimeBag);

    boolean vote(String attribute, S subject, RuntimeBag runtimeBag);
}
