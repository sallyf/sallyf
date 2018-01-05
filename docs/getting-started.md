# Getting started

```java
public class Main
{
    public static void main(String[] args) throws FrameworkException
    {
        Kernel app = Kernel.newInstance();

        Container c = app.getContainer();
        
        app.boot();
        
        Router router = c.get(Router.class);

        router.addController(AppController.class);

        app.start();
    }
}
```

1. Create a `Kernel` instance (`Kernel.newInstance()`)
2. Register your Services in the `Container` (`app.getContainer()`)
3. Boot the `app`
4. Register your Routes in the `Router` (`router.addController(<controller>)`)
5. Start the `app` !
