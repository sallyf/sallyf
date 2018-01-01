# Getting started

```java
public class Main
{
    public static void main(String[] args) throws FrameworkException
    {
        Kernel app = Kernel.newInstance();

        Container c = app.getContainer();
        Router router = c.get(Router.class);

        router.addController(AppController.class);

        app.boot();
    }
}
```

1. Create a `Kernel` instance (`Kernel.newInstance()`)
2. Register your Services in the `Container` (`app.getContainer()`)
3. Register your Routes in the `Router` (`router.addController(<controller>)`)
4. Start the `app` !
