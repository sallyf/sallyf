package com.sallyf.sallyf.Container;

public class ServiceWithoutDefaultConfiguration implements ContainerAwareInterface
{
    public ConfigurationInterface configuration;

    public ServiceWithoutDefaultConfiguration(ConfigurationInterface configuration)
    {
        this.configuration = configuration;
    }
}
