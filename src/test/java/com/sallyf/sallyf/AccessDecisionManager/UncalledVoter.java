package com.sallyf.sallyf.AccessDecisionManager;

import com.sallyf.sallyf.AccessDecisionManager.Voter.VoterInterface;

public class UncalledVoter implements VoterInterface<String>
{
    @Override
    public boolean supports(String attribute, Object subject)
    {
        return false;
    }

    @Override
    public boolean vote(String attribute, String subject)
    {
        return false;
    }
}
