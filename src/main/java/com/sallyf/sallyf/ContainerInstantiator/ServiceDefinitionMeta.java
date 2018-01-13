package com.sallyf.sallyf.ContainerInstantiator;

import com.sallyf.sallyf.Container.MethodCallDefinition;
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

    private ArrayList<MethodCallDefinitionMeta> methodCallDefinitionMetas = new ArrayList<>();

    private HashSet<MethodCallDefinition> methodCallDefinitionsWithMeta = new HashSet<>();

    public ServiceDefinitionMeta(ServiceDefinition<T> serviceDefinition)
    {
        this.serviceDefinition = serviceDefinition;

        updateMethodCallDefinitionMetas();
    }

    public boolean updateMethodCallDefinitionMetas()
    {
        ChangeTracker ct = new ChangeTracker();

        for (MethodCallDefinition methodCallDefinition : serviceDefinition.getMethodCallDefinitions()) {
            if (!methodCallDefinitionsWithMeta.contains(methodCallDefinition)) {
                methodCallDefinitionMetas.add(new MethodCallDefinitionMeta(this, methodCallDefinition));
                methodCallDefinitionsWithMeta.add(methodCallDefinition);

                ct.apply(true);
            }
        }

        return ct.isChanged();
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

    public List<MethodCallDefinitionMeta> getMethodCallDefinitionMetas()
    {
        return methodCallDefinitionMetas;
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
        for (MethodCallDefinitionMeta methodCallDefinitionMeta : getMethodCallDefinitionMetas()) {
            if (!methodCallDefinitionMeta.isCalled()) {
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
