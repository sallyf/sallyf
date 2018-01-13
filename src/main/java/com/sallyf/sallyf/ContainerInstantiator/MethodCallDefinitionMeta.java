package com.sallyf.sallyf.ContainerInstantiator;

import com.sallyf.sallyf.Container.MethodCallDefinition;

public class MethodCallDefinitionMeta
{
    private ServiceDefinitionMeta serviceDefinitionMeta;

    public MethodCallDefinition methodCallDefinition;

    private boolean called = false;

    public MethodCallDefinitionMeta(ServiceDefinitionMeta serviceDefinitionMeta, MethodCallDefinition methodCallDefinition)
    {
        this.serviceDefinitionMeta = serviceDefinitionMeta;
        this.methodCallDefinition = methodCallDefinition;
    }

    public MethodCallDefinition getMethodCallDefinition()
    {
        return methodCallDefinition;
    }

    public boolean isCalled()
    {
        return called;
    }

    public void setCalled(boolean called)
    {
        this.called = called;
    }

    public ServiceDefinitionMeta getServiceDefinitionMeta()
    {
        return serviceDefinitionMeta;
    }
}
