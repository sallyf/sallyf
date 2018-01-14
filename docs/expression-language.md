# ExpressionLanguage

The `ExpressionLanguage` is a string, executed as Javascript.

Some variables are available:

- `container` the `Container`
- `$` the `RuntimeBag`

All `RouteParameters` are automatically injected as individual parameters in the JS Engine.

## Usage

    Integer result = expressionLanguage.evaluate("1 + 1");
