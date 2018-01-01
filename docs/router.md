# Router

The router takes care of storing the app routes and matching them with an `HTTPSession`.

We will assume that `router` is an instance of `Router`.

## Registering a Controller

This is the preferred way of declaring your Route.

An example is the best explanation :

```java
router.registerController(MyController.class);
```

And `MyController.java`:

```java
public class MyController extends BaseController
{
    @Route(path = "/hello/{name}")
    public String helloAction(RouteParameters parameters)
    {
        String name = parameters.get("name");

        return "Hello, " + name + "!";
    }
}
```

Here the controller declares one route matching `/hello/{name}`, `name` being a URL parameter.
The return type is free, see [ResponseTransformer](response-transformer.md).

In an action , several instances can be injected:

- the `HTTPSession`
- the `RoutePatameters`
- any service from the `Container`
