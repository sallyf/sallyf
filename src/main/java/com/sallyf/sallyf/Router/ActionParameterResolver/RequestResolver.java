package com.sallyf.sallyf.Router.ActionParameterResolver;

import com.sallyf.sallyf.Router.ActionParameterResolverInterface;
import com.sallyf.sallyf.Server.RuntimeBag;
import org.eclipse.jetty.server.Request;

import java.lang.reflect.Parameter;

public class RequestResolver implements ActionParameterResolverInterface
{
    @Override
    public boolean supports(Parameter parameter, RuntimeBag runtimeBag)
    {
        return parameter.getType() == Request.class;
    }

    @Override
    public Object resolve(Parameter parameter, RuntimeBag runtimeBag)
    {
        return runtimeBag.getRequest();
    }
}
