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

    private boolean wired = false;

    private boolean initialized = false;

    private ArrayList<CallDefinitionMeta> callDefinitionMetas = new ArrayList<>();

    private HashSet<CallDefinition> callDefinitionsWithMeta = new HashSet<>();

    public ServiceDefinitionMeta(ServiceDefinition<T> serviceDefinition)
    {
        this.serviceDefinition = serviceDefinition;

        updateCallDefinitionMetas();
    }

    public boolean updateCallDefinitionMetas()
    {
        ActivityTracker at = new ActivityTracker();

        for (CallDefinition callDefinition : serviceDefinition.getCallDefinitions()) {
            if (!callDefinitionsWithMeta.contains(callDefinition)) {
                callDefinitionMetas.add(new CallDefinitionMeta(this, callDefinition));
                callDefinitionsWithMeta.add(callDefinition);

                at.apply(true);
            }
        }

        return at.isChanged();
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

    public boolean isWired()
    {
        return wired;
    }

    public void setWired(boolean wired)
    {
        this.wired = wired;
    }

    public boolean isInitialized()
    {
        return initialized;
    }

    public void setInitialized(boolean initialized)
    {
        this.initialized = initialized;
    }

    public boolean isFullyCalled()
    {
        for (CallDefinitionMeta callDefinitionMeta : getCallDefinitionMetas()) {
            if (!callDefinitionMeta.isCalled()) {
                return false;
            }
        }

        return true;
    }

    public boolean isFullyInstantiated()
    {
        return isInstantiated() && isFullyCalled();
    }

    public boolean isReady()
    {
        return isInitialized() && isFullyInstantiated();
    }
}
