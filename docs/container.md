# Container

The `Container` is a shared instance among all the app that enables you to access different `Services`.

We will assume that `container` is an instance of `Container`.

## Registering a service

    container.add(<service class>)
    
A service should extend `ContainerAware` **or** implement `ContainerAwareInterface`
    
## Accessing a service

    container.get(<service class>)
