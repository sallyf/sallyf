package com.sallyf.sallyf.AccessDecisionManager;

import com.sallyf.sallyf.AccessDecisionManager.Voter.VoterInterface;

public class ContainsAVoter implements VoterInterface<String>
{
    @Override
    public boolean supports(String attribute, Object subject)
    {
        if (!(subject instanceof String)) {
            return false;
        }

        return true;
    }

    @Override
    public boolean vote(String attribute, String subject)
    {
        return subject.toLowerCase().contains("a");
    }
}
