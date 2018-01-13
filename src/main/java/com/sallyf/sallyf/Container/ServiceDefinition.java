package com.sallyf.sallyf.Container;

import java.util.ArrayList;

public class ServiceDefinition<T extends ContainerAwareInterface> implements ServiceRepresentationInterface
{
    private Class alias;

    private Class<T> type;

    private ArrayList<ConstructorDefinition> constructorDefinitions = new ArrayList<>();

    private ArrayList<MethodCallDefinition> methodCallDefinitions = new ArrayList<>();

    private ArrayList<String> tags = new ArrayList<>();

    private ReferenceInterface configurationReference;

    private boolean autoWire = true;

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

        setConfigurationReference(configuration == null ? new ConfigurationReference() : new PlainReference<>(configuration));

        setAutoWire(true);
    }

    public ServiceDefinition(Class<T> type, ConfigurationInterface configuration, ArrayList<ConstructorDefinition> constructorDefinitions, ArrayList<MethodCallDefinition> methodCallDefinitions)
    {
        this(type, type, configuration, constructorDefinitions, methodCallDefinitions);
    }

    public ServiceDefinition(Class alias, Class<T> type, ConfigurationInterface configuration, ArrayList<ConstructorDefinition> constructorDefinitions, ArrayList<MethodCallDefinition> methodCallDefinitions)
    {
        this(alias, type, configuration);

        setAutoWire(false);

        setConstructorDefinitions(constructorDefinitions);
        setMethodCallDefinitions(methodCallDefinitions);
    }

    public boolean isAutoWire()
    {
        return autoWire;
    }

    public ServiceDefinition<T> setAutoWire(boolean autoWire)
    {
        this.autoWire = autoWire;

        return this;
    }

    public ReferenceInterface getConfigurationReference()
    {
        return configurationReference;
    }

    public ServiceDefinition<T> setConfigurationReference(ReferenceInterface configurationReference)
    {
        this.configurationReference = configurationReference;

        return this;
    }

    public ArrayList<ConstructorDefinition> getConstructorDefinitions()
    {
        return constructorDefinitions;
    }

    public ServiceDefinition<T> setConstructorDefinitions(ArrayList<ConstructorDefinition> constructorDefinitions)
    {
        this.constructorDefinitions = constructorDefinitions;

        return this;
    }

    public ServiceDefinition<T> addConstructorDefinition(ConstructorDefinition constructorDefinition)
    {
        this.constructorDefinitions.add(constructorDefinition);

        return this;
    }

    public ArrayList<MethodCallDefinition> getMethodCallDefinitions()
    {
        return methodCallDefinitions;
    }

    public ServiceDefinition<T> setMethodCallDefinitions(ArrayList<MethodCallDefinition> methodCallDefinitions)
    {
        this.methodCallDefinitions = methodCallDefinitions;

        return this;
    }

    public ServiceDefinition<T> addMethodCallDefinitions(MethodCallDefinition methodCallDefinition)
    {
        this.methodCallDefinitions.add(methodCallDefinition);

        return this;
    }

    public Class getAlias()
    {
        return alias;
    }

    public Class<T> getType()
    {
        return type;
    }

    public ArrayList<String> getTags()
    {
        return tags;
    }

    public ServiceDefinition<T> addTag(String tag)
    {
        tags.add(tag);

        return this;
    }
}
