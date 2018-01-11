# AccessDecisionManager

## Voter

Each `Voter` should implement `VoterInterface`.

## Strategies

Is an enum in `DecisionStrategy` :

- **AFFIRMATIVE**: At lease 1 voter grants access

- **CONSENSUS**: More voter are granting access than denying

- **UNANIMOUS**: All voters grant access

## Usage

    decisionManager.vote(<attribute>, <subject>, <runtimeBag>)

- `<attribute>` is a string. It is advised to store it as a `public static final` to improve reusability.
- `<subject>` the object to vote on
- `<runtimeBag>` the runtimeBag
