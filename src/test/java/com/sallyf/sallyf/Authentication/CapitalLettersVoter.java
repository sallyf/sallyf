package com.sallyf.sallyf.Authentication;

import com.sallyf.sallyf.Authentication.Voter.VoterInterface;
import com.sallyf.sallyf.Server.RuntimeBag;

import java.util.Arrays;

public class CapitalLettersVoter implements VoterInterface<String>
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
        return subject.toUpperCase().equals(subject);
    }
}
