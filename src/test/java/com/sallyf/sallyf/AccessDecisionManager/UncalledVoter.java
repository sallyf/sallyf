package com.sallyf.sallyf.AccessDecisionManager;

import com.sallyf.sallyf.AccessDecisionManager.Voter.VoterInterface;
import com.sallyf.sallyf.Server.RuntimeBag;

public class UncalledVoter implements VoterInterface<String>
{
    @Override
    public boolean supports(String attribute, Object subject, RuntimeBag runtimeBag)
    {
        return false;
    }

    @Override
    public boolean vote(String attribute, String subject, RuntimeBag runtimeBag)
    {
        return false;
    }
}
