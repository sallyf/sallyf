package com.raphaelvigee.sally.Router.ActionParameterResolver;

import com.raphaelvigee.sally.Container.Container;
import com.raphaelvigee.sally.Container.ContainerAwareInterface;
import com.raphaelvigee.sally.Router.ActionParameterResolverInterface;
import com.raphaelvigee.sally.Server.RuntimeBag;

public class ServiceResolver implements ActionParameterResolverInterface
{
    private Container container;

    public ServiceResolver(Container container)
    {
        this.container = container;
    }

    @Override
    public boolean supports(Class parameterType, RuntimeBag runtimeBag)
    {
        if (ContainerAwareInterface.class.isAssignableFrom(parameterType))
            if (container.has(parameterType))
                return true;
        return false;
    }

    @Override
    public Object resolve(Class parameterType, RuntimeBag runtimeBag)
    {
        return container.get(parameterType);
    }
}
