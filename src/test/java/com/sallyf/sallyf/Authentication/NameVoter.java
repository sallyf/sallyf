package com.sallyf.sallyf.Authentication;

import com.sallyf.sallyf.AccessDecisionManager.Voter.VoterInterface;

import java.util.Arrays;

public class NameVoter implements VoterInterface<String>
{
    public static final String ACCESS = "access";

    @Override
    public boolean supports(String attribute, Object subject)
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
    public boolean vote(String attribute, String subject)
    {
        subject = subject.toLowerCase();

        return subject.equals("admin");
    }
}
