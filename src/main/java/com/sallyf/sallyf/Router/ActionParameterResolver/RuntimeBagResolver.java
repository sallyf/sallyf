package com.sallyf.sallyf.Router.ActionParameterResolver;

import com.sallyf.sallyf.Router.ActionParameterResolverInterface;
import com.sallyf.sallyf.Server.RuntimeBag;

public class RuntimeBagResolver implements ActionParameterResolverInterface
{
    @Override
    public boolean supports(Class parameterType, RuntimeBag runtimeBag)
    {
        return parameterType == RuntimeBag.class;
    }

    @Override
    public Object resolve(Class parameterType, RuntimeBag runtimeBag)
    {
        return runtimeBag;
    }
}
