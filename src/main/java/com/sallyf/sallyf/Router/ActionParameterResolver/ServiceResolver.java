package com.sallyf.sallyf.Router.ActionParameterResolver;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ServiceInterface;
import com.sallyf.sallyf.Router.ActionParameterResolverInterface;
import com.sallyf.sallyf.Server.RuntimeBag;

import java.lang.reflect.Parameter;

public class ServiceResolver implements ActionParameterResolverInterface
{
    private Container container;

    public ServiceResolver(Container container)
    {
        this.container = container;
    }

    @Override
    public boolean supports(Parameter parameter, RuntimeBag runtimeBag)
    {
        if (ServiceInterface.class.isAssignableFrom(parameter.getType())) {
            if (container.has(parameter.getType())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Object resolve(Parameter parameter, RuntimeBag runtimeBag)
    {
        return container.get(parameter.getType().asSubclass(ServiceInterface.class));
    }
}
