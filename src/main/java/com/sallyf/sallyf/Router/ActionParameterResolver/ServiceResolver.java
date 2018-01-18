package com.sallyf.sallyf.Router.ActionParameterResolver;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ServiceInterface;
import com.sallyf.sallyf.Router.ActionParameterResolverInterface;
import com.sallyf.sallyf.Server.RuntimeBag;

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
        if (ServiceInterface.class.isAssignableFrom(parameterType)) {
            if (container.has(parameterType)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Object resolve(Class parameterType, RuntimeBag runtimeBag)
    {
        return container.get(parameterType);
    }
}
