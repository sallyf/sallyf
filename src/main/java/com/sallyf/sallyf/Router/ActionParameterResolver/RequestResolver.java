package com.sallyf.sallyf.Router.ActionParameterResolver;

import com.sallyf.sallyf.Router.ActionParameterResolverInterface;
import com.sallyf.sallyf.Server.RuntimeBag;
import org.eclipse.jetty.server.Request;

public class RequestResolver implements ActionParameterResolverInterface
{
    @Override
    public boolean supports(Class parameterType, RuntimeBag runtimeBag)
    {
        return parameterType == Request.class;
    }

    @Override
    public Object resolve(Class parameterType, RuntimeBag runtimeBag)
    {
        return runtimeBag.getRequest();
    }
}
