# ResponseTransformer

The `ResponseTransformer` is used to transform the response from an action to an instance of `Response`.

## Register the Transformer

```java
public class PrimitiveTransformer implements ResponseTransformerInterface
{
    @Override
    public boolean supports(RuntimeBag runtimeBag, Object response)
    {
        return response instanceof String || response instanceof Number;
    }

    @Override
    public Response resolve(RuntimeBag runtimeBag, Object response)
    {
        return new Response(response.toString());
    }
}
```

Register the transformer in the `Router`:

```java
router.addResponseTransformer(new PrimitiveTransformer());
```
