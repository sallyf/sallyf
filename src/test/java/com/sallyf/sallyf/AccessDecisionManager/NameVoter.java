package com.sallyf.sallyf.AccessDecisionManager;

import com.sallyf.sallyf.AccessDecisionManager.Voter.VoterInterface;
import com.sallyf.sallyf.Server.RuntimeBag;

import java.util.Arrays;

public class NameVoter implements VoterInterface<String>
{
    @Override
    public boolean supports(String attribute, Object subject, RuntimeBag runtimeBag)
    {
        if (!(subject instanceof String)) {
            return false;
        }

        return true;
    }

    @Override
    public boolean vote(String attribute, String subject, RuntimeBag runtimeBag)
    {
        subject = subject.toLowerCase();

        return subject.equals("admin") || subject.equals("foo");
    }
}
