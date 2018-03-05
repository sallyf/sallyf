package com.sallyf.sallyf.Router;

import java.lang.reflect.Parameter;

public interface ActionParameterResolverInterface<R>
{
    boolean supports(Parameter parameter);

    R resolve(Parameter parameter);
}
