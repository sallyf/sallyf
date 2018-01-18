package com.sallyf.sallyf.FlashManager;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Router.ActionParameterResolverInterface;
import com.sallyf.sallyf.Server.RuntimeBag;

public class FlashManagerHelperActionParameterResolver implements ActionParameterResolverInterface<FlashManagerHelper>
{
    private Container container;

    public FlashManagerHelperActionParameterResolver(Container container)
    {
        this.container = container;
    }

    @Override
    public boolean supports(Class parameterType, RuntimeBag runtimeBag)
    {
        return parameterType == FlashManagerHelper.class;
    }

    @Override
    public FlashManagerHelper resolve(Class parameterType, RuntimeBag runtimeBag)
    {
        return new FlashManagerHelper(container.get(FlashManager.class), runtimeBag);
    }
}
