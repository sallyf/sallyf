# RouteParameterResolver

The `RouteParameterResolver` is used to transform the `RouteParameters` from the URL into its appropriate representation.

Example:

The `Route`:

    /user/{user}

When called with the URL:

    /user/3
    
Would result in having a parameter named `user` with the value `3`.

It would be more interesting to have it directly resolved to the appropriate `User` from a database for example.

## Register the Resolver

```java
public class UserResolver implements RouteParameterResolverInterface<User>
{
    @Override
    public boolean supports(String name, String value, HTTPSession session)
    {
        return Objects.equals(name, "user");
    }

    @Override
    public User resolve(String name, String value, HTTPSession session)
    {
        User user = fetchFromDB(value); 
        
        // Custom logic...
        
        return user;
    }
}
```

Register the resolver in the `Router`:

```java
router.addRouteParameterResolver(new UserResolver());
```
