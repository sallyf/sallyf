package com.sallyf.sallyf.Container;

public class DefaultConfigurationReference<T extends ContainerAwareInterface> implements ReferenceInterface
{
    ServiceReference<T> serviceReference;

    public DefaultConfigurationReference(Class<T> type)
    {
        this.serviceReference = new ServiceReference<>(type);
    }
}
