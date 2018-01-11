package com.sallyf.sallyf.AccessDecisionManager;

import com.sallyf.sallyf.AccessDecisionManager.Voter.VoterInterface;
import com.sallyf.sallyf.Authentication.DecisionStrategy;
import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ContainerAwareInterface;
import com.sallyf.sallyf.Server.RuntimeBag;

import java.util.ArrayList;

public class AccessDecisionManager implements ContainerAwareInterface
{
    public static final String TAG_VOTER = "authentication.voter";

    private Container container;

    public AccessDecisionManager(Container container)
    {
        this.container = container;
    }

    public <O> boolean vote(String attribute, O subject, RuntimeBag runtimeBag)
    {
        return vote(attribute, subject, runtimeBag, DecisionStrategy.AFFIRMATIVE);
    }

    public <O> boolean vote(String attribute, O subject, RuntimeBag runtimeBag, DecisionStrategy strategy)
    {
        ArrayList<Boolean> decisions = new ArrayList<>();

        for (VoterInterface<O> voter : container.<VoterInterface<O>>getByTag(TAG_VOTER)) {
            if (voter.supports(attribute, subject, runtimeBag)) {
                boolean d = voter.vote(attribute, subject, runtimeBag);

                decisions.add(d);
            }
        }

        return decide(decisions, strategy);
    }

    private boolean decide(ArrayList<Boolean> decisions, DecisionStrategy strategy)
    {
        if (decisions.isEmpty()) {
            return true;
        }

        switch (strategy) {
            case AFFIRMATIVE:
                return decisions.contains(true);
            case CONSENSUS:
                long countTrue = decisions.stream().filter(d -> d == true).count();
                long countFalse = decisions.stream().filter(d -> d == false).count();

                return countTrue > countFalse;
            case UNANIMOUS:
                return !decisions.contains(false);
        }

        return false; // Should never be reached
    }
}
