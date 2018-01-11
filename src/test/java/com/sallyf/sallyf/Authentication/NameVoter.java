package com.sallyf.sallyf.Authentication;

import com.sallyf.sallyf.AccessDecisionManager.Voter.VoterInterface;
import com.sallyf.sallyf.Server.RuntimeBag;

import java.util.Arrays;

public class NameVoter implements VoterInterface<String>
{
    public static final String ACCESS = "access";

    @Override
    public boolean supports(String attribute, Object subject, RuntimeBag runtimeBag)
    {
        if (!Arrays.asList(new String[]{ACCESS}).contains(attribute)) {
            return false;
        }

        if (!(subject instanceof String)) {
            return false;
        }

        return true;
    }

    @Override
    public boolean vote(String attribute, String subject, RuntimeBag runtimeBag)
    {
        subject = subject.toLowerCase();

        return subject.equals("admin");
    }
}
