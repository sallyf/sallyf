package com.sallyf.sallyf.Container;

public class ServiceWithoutDefaultConfiguration implements ServiceInterface
{
    public ConfigurationInterface configuration;

    public ServiceWithoutDefaultConfiguration(ConfigurationInterface configuration)
    {
        this.configuration = configuration;
    }
}
