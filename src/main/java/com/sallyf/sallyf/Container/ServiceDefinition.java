package com.sallyf.sallyf.Container;

import java.util.ArrayList;

public class ServiceDefinition<T extends ContainerAwareInterface>
{
    public Class alias;

    public Class<T> type;

    public ArrayList<ConstructorDefinition> constructorDefinitions = new ArrayList<>();

    public ArrayList<CallDefinition> callDefinitions = new ArrayList<>();

    public ReferenceInterface configurationReference;

    boolean autoConfigure = true;

    public ServiceDefinition(Class<T> type)
    {
        this(type, type);
    }

    public ServiceDefinition(Class alias, Class<T> type)
    {
        this(alias, type, null);
    }

    public ServiceDefinition(Class<T> type, ConfigurationInterface configuration)
    {
        this(type, type, configuration);
    }

    public ServiceDefinition(Class alias, Class<T> type, ConfigurationInterface configuration)
    {
        this.type = type;
        this.alias = alias;

        this.configurationReference = configuration == null ? new ConfigurationReference() : new PlainReference<>(configuration);

        this.autoConfigure = true;
    }

    public ServiceDefinition(Class alias, Class<T> type, ConfigurationInterface configuration, ArrayList<ConstructorDefinition> constructorDefinitions, ArrayList<CallDefinition> callDefinitions)
    {
        this(alias, type, configuration);

        this.autoConfigure = false;

        this.constructorDefinitions = constructorDefinitions;
        this.callDefinitions = callDefinitions;
    }

    public ServiceDefinition(Class<T> type, ConfigurationInterface configuration, ArrayList<ConstructorDefinition> constructorDefinitions, ArrayList<CallDefinition> callDefinitions)
    {
        this(type, type, configuration, constructorDefinitions, callDefinitions);

        this.autoConfigure = false;
    }
}
