# Security

The Routes access can be secured by adding an `@Security` annotation to them, containing an [expression](expression-language.md).

## Usage

```java
public class MyController extends BaseController
{
    @Route(path = "/secured")
    @Security("is_granted($, 'authenticated')")
    public String securedAction()
    {
        return "Secured area";
    }
}
```

The `securedAction` can now only be accessed by authenticated users
