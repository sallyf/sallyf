package com.sallyf.sallyf.Container;

import com.sallyf.sallyf.Container.Exception.AmbiguousServiceException;
import com.sallyf.sallyf.Container.Exception.ContainerInstantiatedException;
import com.sallyf.sallyf.Container.Exception.ServiceInstantiationException;
import com.sallyf.sallyf.Container.ReferenceResolver.ConfigurationReferenceResolver;
import com.sallyf.sallyf.Container.ReferenceResolver.ContainerReferenceResolver;
import com.sallyf.sallyf.Container.ReferenceResolver.PlainReferenceResolver;
import com.sallyf.sallyf.Container.ReferenceResolver.ServiceReferenceResolver;
import com.sallyf.sallyf.Container.TypeResolver.ConfigurationResolver;
import com.sallyf.sallyf.Container.TypeResolver.ContainerResolver;
import com.sallyf.sallyf.Container.TypeResolver.ServiceResolver;
import com.sallyf.sallyf.ContainerInstantiator.ContainerInstantiator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Container
{
    private ContainerInstantiator containerInstantiator;

    private Map<Class, ContainerAwareInterface> services = new HashMap<>();

    private Map<String, ArrayList<ContainerAwareInterface>> taggedServices = new HashMap<>();

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

        containerInstantiator.addServiceDefinition(serviceDefinition);

        return serviceDefinition;
    }

    public ServiceDefinition<?> getServiceDefinition(Class type)
    {
        return containerInstantiator.getServiceDefinitions().get(type);
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
        ArrayList<T> services = (ArrayList<T>) taggedServices.get(tag);

        if (null == services) {
            return new ArrayList<>();
        }

        return services;
    }

    public <T extends ContainerAwareInterface> T get(Class<T> serviceClass)
    {
        return (T) services.get(serviceClass);
    }

    public <T extends ContainerAwareInterface> T find(String name) throws AmbiguousServiceException
    {
        return get(findAlias(name));
    }

    public <T extends ContainerAwareInterface> Class<T> findAlias(String name) throws AmbiguousServiceException
    {
        List<Class> serviceClasses = this.services.entrySet().stream()
                .filter(e -> {
                    Class alias = e.getKey();

                    if (alias.getName().endsWith(name)) {
                        return true;
                    }

                    ContainerAwareInterface service = e.getValue();

                    return service.getClass().getName().endsWith(name);
                })
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (serviceClasses.isEmpty()) {
            return null;
        }

        if (serviceClasses.size() > 1) {
            throw new AmbiguousServiceException("Ambiguous service name: " + name);
        }

        return (Class<T>) serviceClasses.get(0);
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
