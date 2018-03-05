package com.sallyf.sallyf.AccessDecisionManager;

import com.sallyf.sallyf.AccessDecisionManager.Voter.VoterInterface;
import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ServiceInterface;
import com.sallyf.sallyf.ExpressionLanguage.ExpressionLanguage;
import com.sallyf.sallyf.Server.RuntimeBag;

import java.util.ArrayList;

public class AccessDecisionManager implements ServiceInterface
{
    public static final String TAG_VOTER = "authentication.voter";

    private Container container;

    private ExpressionLanguage expressionLanguage;

    public AccessDecisionManager(Container container, ExpressionLanguage expressionLanguage)
    {
        this.container = container;
        this.expressionLanguage = expressionLanguage;
    }

    @Override
    public void initialize(Container container)
    {
        expressionLanguage.addFunction("is_granted", request -> {
            request.arguments(2, 4);

            if (request.getArgs().length == 2) {
                return vote(request.get(0), request.get(1), null);
            }

            if (request.getArgs().length == 3) {
                return vote(request.get(0), request.get(1), request.get(2));
            }

            return vote(request.get(0), request.get(1), request.get(2), request.get(3));
        });
    }

    public <O> boolean vote(RuntimeBag runtimeBag, String attribute, O subject)
    {
        return vote(runtimeBag, attribute, subject, DecisionStrategy.AFFIRMATIVE);
    }

    public <O> boolean vote(RuntimeBag runtimeBag, String attribute, O subject, DecisionStrategy strategy)
    {
        ArrayList<Boolean> decisions = new ArrayList<>();

        for (VoterInterface<O> voter : container.<VoterInterface<O>>getByTag(TAG_VOTER)) {
            if (voter.supports(attribute, subject)) {
                boolean d = voter.vote(attribute, subject);

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
