# Container

The `Container` is a shared instance among all the app that enables you to access different `Services`.

We will assume that `container` is an instance of `Container`.

## Registering a service

A service should implement `ContainerAwareInterface`. They must be added before the kernel is booted.

The constructor signature is free since the services are dynamically resolved.

### Auto wiring

    container.add(new ServiceDefinition<>(<service class>));    

### Manual Definition

`ServiceDefinition` takes different parameters:

- `Class` **alias** : The class the Service is registered as
- `Class<T>` **type** : The actual Service class
- `ConfigurationInterface` **configuration** : A `ConfigurationInterface`
- `ArrayList<ConstructorDefinition>` **constructorDefinitions** : See [ConstructorDefinition](#constructordefinition)
- `ArrayList<CallDefinition>` **callDefinitions** : See [CallDefinition](#calldefinition)

#### ConstructorDefinition

It represents a constructor signature; it accepts [`ReferenceInterface`](#referenceinterface)'s

#### CallDefinition

It represents a method signature; it accepts a method name and [`ReferenceInterface`](#referenceinterface)'s

### ReferenceInterface

Several references are available :

- `ContainerReference` : Represents the `Container`
- `DefaultConfigurationReference` : Represents the default service configuration
- `PlainReference` : Represents a plain value
- `ServiceReference` : Represents a `Service`

    
## Accessing a service

    container.get(<service class>)
