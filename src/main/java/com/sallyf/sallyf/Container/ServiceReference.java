package com.sallyf.sallyf.Container;

public class ServiceReference<T extends ContainerAwareInterface> implements ReferenceInterface
{
    private Class<T> type;

    public ServiceReference(Class<T> type)
    {
        this.type = type;
    }

    public Class<T> getType()
    {
        return type;
    }
}
