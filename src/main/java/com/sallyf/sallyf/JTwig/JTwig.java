package com.sallyf.sallyf.JTwig;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ServiceInterface;
import com.sallyf.sallyf.EventDispatcher.EventDispatcher;
import com.sallyf.sallyf.Exception.FrameworkException;
import com.sallyf.sallyf.KernelEvents;
import com.sallyf.sallyf.Router.Router;
import org.jtwig.environment.EnvironmentConfiguration;
import org.jtwig.environment.EnvironmentConfigurationBuilder;

public class JTwig implements ServiceInterface
{
    private final Router router;

    private EnvironmentConfiguration configuration;

    public JTwig(EventDispatcher eventDispatcher, Router router)
    {
        this.router = router;

        eventDispatcher.register(KernelEvents.STARTED, (t, e) -> {
            configuration = EnvironmentConfigurationBuilder
                    .configuration()
                    .build();
        });
    }

    @Override
    public void initialize(Container container)
    {
        router.addResponseTransformer(new JTwigResponseTransformer(this));
    }

    public EnvironmentConfiguration getConfiguration()
    {
        if (configuration == null) {
            throw new FrameworkException("JTwig configuration not built yet");
        }

        return configuration;
    }
}
