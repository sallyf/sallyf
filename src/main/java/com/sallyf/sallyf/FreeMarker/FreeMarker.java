package com.sallyf.sallyf.FreeMarker;


import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ServiceInterface;
import com.sallyf.sallyf.Router.Router;

public class FreeMarker implements ServiceInterface
{
    private final Router router;

    private Configuration configuration;

    public FreeMarker(Router router, Configuration configuration)
    {
        this.router = router;
        this.configuration = configuration;
    }

    @Override
    public void initialize(Container container)
    {
        router.addResponseTransformer(new FreeMarkerTransformer(this));
    }

    public Configuration getConfiguration()
    {
        return configuration;
    }
}
