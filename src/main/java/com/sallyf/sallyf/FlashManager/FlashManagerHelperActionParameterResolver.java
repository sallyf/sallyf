package com.sallyf.sallyf.FlashManager;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Router.ActionParameterResolverInterface;
import com.sallyf.sallyf.Server.RuntimeBag;

import java.lang.reflect.Parameter;

public class FlashManagerHelperActionParameterResolver implements ActionParameterResolverInterface<FlashManagerHelper>
{
    private Container container;

    public FlashManagerHelperActionParameterResolver(Container container)
    {
        this.container = container;
    }

    @Override
    public boolean supports(Parameter parameter, RuntimeBag runtimeBag)
    {
        return parameter.getType() == FlashManagerHelper.class;
    }

    @Override
    public FlashManagerHelper resolve(Parameter parameter, RuntimeBag runtimeBag)
    {
        return new FlashManagerHelper(container.get(FlashManager.class), runtimeBag);
    }
}
