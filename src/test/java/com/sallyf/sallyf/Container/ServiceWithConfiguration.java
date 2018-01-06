package com.sallyf.sallyf.Container;

public class ServiceWithConfiguration implements ContainerAwareInterface
{
    public DefaultServiceConfiguration configuration;

    public ServiceWithConfiguration(DefaultServiceConfiguration configuration)
    {
        this.configuration = configuration;
    }

    public static Class<? extends ConfigurationInterface> getDefaultConfigurationClass()
    {
        return DefaultServiceConfiguration.class;
    }

    public static class CustomServiceConfiguration extends DefaultServiceConfiguration
    {
        @Override
        public int getNumber()
        {
            return 2;
        }
    }

    public static class DefaultServiceConfiguration implements ConfigurationInterface
    {
        public int getNumber()
        {
            return 1;
        }
    }
}
