package com.sallyf.sallyf.Container;

public class ServiceReference<T extends ContainerAwareInterface> implements ReferenceInterface, ServiceAliasAwareInterface
{
    private Class<T> alias;

    public ServiceReference(Class<T> alias)
    {
        this.alias = alias;
    }

    public Class<T> getAlias()
    {
        return alias;
    }
}
