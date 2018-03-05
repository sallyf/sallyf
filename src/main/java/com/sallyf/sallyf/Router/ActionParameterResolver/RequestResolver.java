package com.sallyf.sallyf.Router.ActionParameterResolver;

import com.sallyf.sallyf.Router.ActionParameterResolverInterface;
import com.sallyf.sallyf.Server.RuntimeBag;
import com.sallyf.sallyf.Server.RuntimeBagContext;
import org.eclipse.jetty.server.Request;

import java.lang.reflect.Parameter;

public class RequestResolver implements ActionParameterResolverInterface
{
    @Override
    public boolean supports(Parameter parameter)
    {
        return parameter.getType() == Request.class;
    }

    @Override
    public Object resolve(Parameter parameter)
    {
        RuntimeBag runtimeBag = RuntimeBagContext.get();

        return runtimeBag.getRequest();
    }
}
