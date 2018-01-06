package com.sallyf.sallyf.Container;

public class ServiceReference<T extends ContainerAwareInterface> implements ReferenceInterface
{
    public Class<T> type;

    public ServiceReference(Class<T> type)
    {
        this.type = type;
    }
}
