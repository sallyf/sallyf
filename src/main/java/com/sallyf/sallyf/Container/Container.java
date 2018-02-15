package com.sallyf.sallyf.Container;

import com.sallyf.sallyf.Container.Exception.AmbiguousServiceException;
import com.sallyf.sallyf.Container.ReferenceResolver.ConfigurationReferenceResolver;
import com.sallyf.sallyf.Container.ReferenceResolver.ContainerReferenceResolver;
import com.sallyf.sallyf.Container.ReferenceResolver.PlainReferenceResolver;
import com.sallyf.sallyf.Container.ReferenceResolver.ServiceReferenceResolver;
import com.sallyf.sallyf.Container.TypeResolver.ConfigurationResolver;
import com.sallyf.sallyf.Container.TypeResolver.ContainerResolver;
import com.sallyf.sallyf.Container.TypeResolver.ServiceResolver;
import com.sallyf.sallyf.ContainerInstantiator.ContainerInstantiator;
import com.sallyf.sallyf.Exception.NonExistentServiceException;

import java.util.*;
import java.util.stream.Collectors;

public class Container
{
    private ContainerInstantiator containerInstantiator;

    private Map<Class, ServiceInterface> services = new HashMap<>();

    private Map<String, ArrayList<ServiceInterface>> taggedServices = new HashMap<>();

    private boolean instantiated = false;

    private boolean instantiating = false;

    public Container()
    {
        containerInstantiator = new ContainerInstantiator(this, services, taggedServices);

        addReferenceResolver(new PlainReferenceResolver());
        addReferenceResolver(new ConfigurationReferenceResolver(this));
        addReferenceResolver(new ContainerReferenceResolver(this));
        addReferenceResolver(new ServiceReferenceResolver(this));

        addTypeResolver(new ConfigurationResolver());
        addTypeResolver(new ContainerResolver());
        addTypeResolver(new ServiceResolver());
    }

    public void addAll(ServiceDefinition<? extends ServiceInterface>[] serviceDefinitions)
    {
        for (ServiceDefinition<? extends ServiceInterface> serviceDefinition : serviceDefinitions) {
            add(serviceDefinition);
        }
    }

    public <T extends ServiceInterface> ServiceDefinition<T> add(ServiceDefinition<T> serviceDefinition)
    {
        containerInstantiator.addServiceDefinition(serviceDefinition);

        return serviceDefinition;
    }

    public ServiceDefinition<?> getServiceDefinition(Class type)
    {
        return containerInstantiator.getServiceDefinitions().get(type);
    }

    public void instantiate()
    {
        instantiating = true;

        containerInstantiator.boot();

        instantiating = false;
        instantiated = true;

        freeze();
    }

    private void freeze()
    {
        services = Collections.unmodifiableMap(services);
        taggedServices = Collections.unmodifiableMap(taggedServices);
    }

    public <T extends ServiceInterface> ArrayList<T> getByTag(String tag)
    {
        ArrayList<T> services = (ArrayList<T>) taggedServices.get(tag);

        if (null == services) {
            return new ArrayList<>();
        }

        return services;
    }

    public <T extends ServiceInterface> T get(Class<T> serviceClass)
    {
        T service = (T) services.get(serviceClass);

        if (null == service) {
            throw new NonExistentServiceException(serviceClass);
        }

        return service;
    }

    public <T extends ServiceInterface> T find(String name)
    {
        return get(findAlias(name));
    }

    public <T extends ServiceInterface> Class<T> findAlias(String name)
    {
        List<Class> serviceClasses = this.services.entrySet().stream()
                .filter(e -> {
                    Class alias = e.getKey();

                    if (alias.getName().endsWith(name)) {
                        return true;
                    }

                    ServiceInterface service = e.getValue();

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

    public void addReferenceResolver(ReferenceResolverInterface resolver)
    {
        containerInstantiator.getReferenceResolvers().add(resolver);
    }

    public void addTypeResolver(TypeResolverInterface resolver)
    {
        containerInstantiator.getTypeResolvers().add(resolver);
    }

    public Map<Class, ServiceDefinition<? extends ServiceInterface>> getServiceDefinitions()
    {
        return containerInstantiator.getServiceDefinitions();
    }

    public boolean isInstantiated()
    {
        return instantiated;
    }

    public boolean isInstantiating()
    {
        return instantiating;
    }
}
