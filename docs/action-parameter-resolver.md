# ActionParameterResolver

The `ActionParameterResolver` is used to resolve the arguments to provide to an action.

```java
@Route(path = "/container")
public static Response hello4(Container container)
{
    return new Response("This action has the Container !");
}
```

## Register the Resolver

Example, the `ContainerResolver` provides the `Container` to an action:

```java
public class ContainerResolver implements ActionParameterResolverInterface
{
    private Container container;

    public ContainerResolver(Container container)
    {
        this.container = container;
    }

    @Override
    public boolean supports(Class parameterType, RuntimeBag runtimeBag)
    {
        return parameterType == Container.class;
    }

    @Override
    public Object resolve(Class parameterType, RuntimeBag runtimeBag)
    {
        return container;
    }
}
```

Register the resolver in the `Router`:

```java
router.addActionParameterResolver(new ContainerResolver(container));
```
