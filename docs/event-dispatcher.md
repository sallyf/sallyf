# Event Dispatcher

The event dispatcher is a way of triggering generic actions from various places of the codebase.

We will assume that `eventDispatcher` is an instance of `EventDispatcher`.

## Registering an event

To ensure a sane codebase, here is the required way of declaring an EventType:

```java
EventType<EVENT_TYPE_OBJECT> EVENT = new EventType<>("UNIQUE_EVENT_NAME")
```

The event registration:

```java
eventDispatcher.register(EVENT, (eventType, eventObject) -> {
    // eventObject is an instance of `EVENT_TYPE_OBJECT`
    
    // Custom logic
});
```

The event dispatch:

```java
eventDispatcher.dispatch(EVENT, new EVENT_TYPE_OBJECT(...));
```

Your `EVENT_TYPE_OBJECT` can be fully customized.
