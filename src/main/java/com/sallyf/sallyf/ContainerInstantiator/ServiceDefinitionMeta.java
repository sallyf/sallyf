package com.sallyf.sallyf.ContainerInstantiator;

import com.sallyf.sallyf.Container.CallDefinition;
import com.sallyf.sallyf.Container.ContainerAwareInterface;
import com.sallyf.sallyf.Container.ServiceDefinition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ServiceDefinitionMeta<T extends ContainerAwareInterface>
{
    private ServiceDefinition<T> serviceDefinition;

    private boolean instantiated = false;

    private ArrayList<CallDefinitionMeta> callDefinitionMetas = new ArrayList<>();

    private HashSet<CallDefinition> callDefinitionsWithMeta = new HashSet<>();

    public ServiceDefinitionMeta(ServiceDefinition<T> serviceDefinition)
    {
        this.serviceDefinition = serviceDefinition;

        updateCallDefinitionMetas();
    }

    public boolean updateCallDefinitionMetas()
    {
        boolean hasChanged = false;

        for (CallDefinition callDefinition : serviceDefinition.getCallDefinitions()) {
            if (!callDefinitionsWithMeta.contains(callDefinition)) {
                hasChanged = true;

                callDefinitionMetas.add(new CallDefinitionMeta(this, callDefinition));
                callDefinitionsWithMeta.add(callDefinition);
            }
        }

        return hasChanged;
    }

    public ServiceDefinition<T> getServiceDefinition()
    {
        return serviceDefinition;
    }

    public boolean isInstantiated()
    {
        return instantiated;
    }

    public void setInstantiated(boolean instantiated)
    {
        this.instantiated = instantiated;
    }

    public List<CallDefinitionMeta> getCallDefinitionMetas()
    {
        return callDefinitionMetas;
    }
}
