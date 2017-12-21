package com.raphaelvigee.sally.Router.ActionParameterResolver;

import com.raphaelvigee.sally.Router.ActionParameterResolverInterface;
import com.raphaelvigee.sally.Server.RuntimeBag;
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
