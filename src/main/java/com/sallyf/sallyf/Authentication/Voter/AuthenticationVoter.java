package com.sallyf.sallyf.Authentication.Voter;

import com.sallyf.sallyf.AccessDecisionManager.Voter.VoterInterface;
import com.sallyf.sallyf.Authentication.AuthenticationManager;
import com.sallyf.sallyf.Container.ContainerAwareInterface;
import com.sallyf.sallyf.Server.RuntimeBag;

import java.util.Arrays;

public class AuthenticationVoter implements VoterInterface, ContainerAwareInterface
{
    public static final String ANONYMOUS = "anonymous";

    public static final String AUTHENTICATED = "authenticated";

    private AuthenticationManager authenticationManager;

    public AuthenticationVoter(AuthenticationManager authenticationManager)
    {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public boolean supports(String attribute, Object subject, RuntimeBag runtimeBag)
    {
        if (!Arrays.asList(new String[]{ANONYMOUS, AUTHENTICATED}).contains(attribute)) {
            return false;
        }

        return true;
    }

    @Override
    public boolean vote(String attribute, Object subject, RuntimeBag runtimeBag)
    {
        switch (attribute) {
            case ANONYMOUS:
                return isAnonymous(runtimeBag);
            case AUTHENTICATED:
                return isAuthenticated(runtimeBag);
        }

        return false;
    }

    private boolean isAnonymous(RuntimeBag runtimeBag)
    {
        return null == this.authenticationManager.getUser(runtimeBag);
    }

    private boolean isAuthenticated(RuntimeBag runtimeBag)
    {
        return null != this.authenticationManager.getUser(runtimeBag);
    }
}
