package com.sallyf.sallyf.AccessDecisionManager;

public enum DecisionStrategy
{
    AFFIRMATIVE, // At lease 1 voter grants access

    CONSENSUS, // More voter are granting access than denying

    UNANIMOUS, // All voters grant access
}
