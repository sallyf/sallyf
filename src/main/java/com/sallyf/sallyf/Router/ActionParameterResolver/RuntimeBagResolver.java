package com.sallyf.sallyf.Router.ActionParameterResolver;

import com.sallyf.sallyf.Router.ActionParameterResolverInterface;
import com.sallyf.sallyf.Server.RuntimeBag;

import java.lang.reflect.Parameter;

public class RuntimeBagResolver implements ActionParameterResolverInterface
{
    @Override
    public boolean supports(Parameter parameter, RuntimeBag runtimeBag)
    {
        return parameter.getType() == RuntimeBag.class;
    }

    @Override
    public Object resolve(Parameter parameter, RuntimeBag runtimeBag)
    {
        return runtimeBag;
    }
}
