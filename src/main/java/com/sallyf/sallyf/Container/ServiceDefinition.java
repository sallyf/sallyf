package com.sallyf.sallyf.Container;

import java.util.ArrayList;

public class ServiceDefinition<T extends ContainerAwareInterface>
{
    Class alias;

    Class<T> type;

    ArrayList<ConstructorDefinition> constructorDefinitions = new ArrayList<>();

    ArrayList<CallDefinition> callDefinitions = new ArrayList<>();

    public ServiceDefinition(Class<T> type)
    {
        this(type, type);
    }

    public ServiceDefinition(Class alias, Class<T> type)
    {
        this.type = type;
        this.alias = alias;

        constructorDefinitions.add(new ConstructorDefinition(new ContainerReference()));
        constructorDefinitions.add(new ConstructorDefinition());

        callDefinitions.add(new CallDefinition("setContainer", new ContainerReference()));
    }

    public ServiceDefinition(Class alias, Class<T> type, ConfigurationInterface configuration)
    {
        this(alias, type);

        constructorDefinitions.add(new ConstructorDefinition(new ContainerReference(), new PlainReference(configuration)));
    }

    public ServiceDefinition(Class<T> type, ConfigurationInterface configuration)
    {
        this(type, type, configuration);
    }

    public ServiceDefinition(Class alias, Class<T> type, ArrayList<ConstructorDefinition> constructorDefinitions, ArrayList<CallDefinition> callDefinitions)
    {
        this.type = type;
        this.alias = alias;
        this.constructorDefinitions = constructorDefinitions;
        this.callDefinitions = callDefinitions;
    }
}
