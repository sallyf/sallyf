package com.sallyf.sallyf.Container;

public class ConfigurationReference<T extends ContainerAwareInterface> implements ReferenceInterface
{
    Class<T> type;

    public ConfigurationReference(Class<T> type)
    {
        this.type = type;
    }
}
