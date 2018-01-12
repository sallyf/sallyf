package com.sallyf.sallyf.Container;

import com.sallyf.sallyf.Container.Exception.ContainerInstantiatedException;
import com.sallyf.sallyf.Container.Exception.ServiceInstantiationException;
import com.sallyf.sallyf.Container.ReferenceResolver.ConfigurationReferenceResolver;
import com.sallyf.sallyf.Container.ReferenceResolver.ContainerReferenceResolver;
import com.sallyf.sallyf.Container.ReferenceResolver.PlainReferenceResolver;
import com.sallyf.sallyf.Container.ReferenceResolver.ServiceReferenceResolver;
import com.sallyf.sallyf.Container.TypeResolver.ConfigurationResolver;
import com.sallyf.sallyf.Container.TypeResolver.ContainerResolver;
import com.sallyf.sallyf.Container.TypeResolver.ServiceResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Container
{
    private ContainerInstantiator containerInstantiator;

    private Map<Class, ContainerAwareInterface> services = new HashMap<>();

    private Map<String, ArrayList<ContainerAwareInterface>> taggedServices = new HashMap<String, ArrayList<ContainerAwareInterface>>();

    private boolean instantiated = false;

    public Container()
    {
        containerInstantiator = new ContainerInstantiator(services, taggedServices);

        addReferenceResolver(new ConfigurationReferenceResolver(this));
        addReferenceResolver(new ContainerReferenceResolver(this));
        addReferenceResolver(new PlainReferenceResolver());
        addReferenceResolver(new ServiceReferenceResolver(this));

        addTypeResolver(new ConfigurationResolver());
        addTypeResolver(new ContainerResolver());
        addTypeResolver(new ServiceResolver());
    }

    public void addAll(ServiceDefinition<? extends ContainerAwareInterface>[] serviceDefinitions) throws ServiceInstantiationException
    {
        for (ServiceDefinition<? extends ContainerAwareInterface> serviceDefinition : serviceDefinitions) {
            add(serviceDefinition);
        }
    }

    public <T extends ContainerAwareInterface> ServiceDefinition<T> add(ServiceDefinition<T> serviceDefinition) throws ServiceInstantiationException
    {
        if (instantiated) {
            throw new ContainerInstantiatedException();
        }

        containerInstantiator.getServiceDefinitions().put(serviceDefinition.getAlias(), serviceDefinition);

        containerInstantiator.autoWire(serviceDefinition);

        return serviceDefinition;
    }

    public void instantiate() throws ServiceInstantiationException
    {
        if (instantiated) {
            throw new ContainerInstantiatedException();
        }

        containerInstantiator.boot();

        instantiated = true;
    }

    public <T extends ContainerAwareInterface> ArrayList<T> getByTag(String tag)
    {
        ArrayList<ContainerAwareInterface> services = taggedServices.get(tag);

        if (null == services) {
            return new ArrayList<>();
        }

        return (ArrayList<T>) services;
    }

    public <T extends ContainerAwareInterface> T get(Class<T> serviceClass)
    {
        return (T) services.get(serviceClass);
    }

    public <T extends ContainerAwareInterface, C> C get(Class<T> serviceClass, Class<C> castType)
    {
        return (C) get(serviceClass);
    }

    public boolean has(Class serviceClass)
    {
        return services.containsKey(serviceClass);
    }

    public Map<Class, ConfigurationInterface> getConfigurations()
    {
        return containerInstantiator.getConfigurations();
    }

    public <T extends ContainerAwareInterface> void setConfiguration(Class<T> serviceClass, ConfigurationInterface configuration)
    {
        getConfigurations().put(serviceClass, configuration);
    }

    public <T extends ContainerAwareInterface> ConfigurationInterface getConfiguration(Class<T> serviceClass)
    {
        return getConfigurations().get(serviceClass);
    }

    public void addReferenceResolver(ReferenceResolverInterface resolver)
    {
        containerInstantiator.getReferenceResolvers().add(resolver);
    }

    public void addTypeResolver(TypeResolverInterface resolver)
    {
        containerInstantiator.getTypeResolvers().add(resolver);
    }

    public Map<Class, ServiceDefinition<? extends ContainerAwareInterface>> getServiceDefinitions()
    {
        return containerInstantiator.getServiceDefinitions();
    }
}
