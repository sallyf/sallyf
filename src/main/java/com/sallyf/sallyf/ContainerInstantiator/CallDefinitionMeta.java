package com.sallyf.sallyf.ContainerInstantiator;

import com.sallyf.sallyf.Container.CallDefinition;

public class CallDefinitionMeta
{
    private ServiceDefinitionMeta serviceDefinitionMeta;

    public CallDefinition callDefinition;

    private boolean called = false;

    public CallDefinitionMeta(ServiceDefinitionMeta serviceDefinitionMeta, CallDefinition callDefinition)
    {
        this.serviceDefinitionMeta = serviceDefinitionMeta;
        this.callDefinition = callDefinition;
    }

    public CallDefinition getCallDefinition()
    {
        return callDefinition;
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
