package com.sallyf.sallyf.ExpressionLanguage;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ServiceInterface;
import com.sallyf.sallyf.Exception.FrameworkException;
import com.sallyf.sallyf.Router.Router;
import com.sallyf.sallyf.Server.RuntimeBag;
import com.sallyf.sallyf.Server.RuntimeBagContext;
import com.sallyf.sallyf.Utils.MapUtils;

import java.util.HashMap;
import java.util.Map;

import static com.sallyf.sallyf.Utils.MapUtils.entry;

public class ExpressionLanguage extends com.raphaelvigee.el.ExpressionLanguage implements ServiceInterface
{
    private Router router;

    public ExpressionLanguage(Router router)
    {
        super();

        this.router = router;
    }

    @Override
    public void initialize(Container container)
    {
        add("container", container);
        addFunction("service", request -> {
            request.arguments(1);

            String className = request.get(0);

            Class<? extends ServiceInterface> type = null;

            try {
                type = Class.forName(className).asSubclass(ServiceInterface.class);
            } catch (ClassNotFoundException ignored) {
            }

            if (null == type) {
                type = container.findAlias(className);
            }

            if (null == type) {
                throw new FrameworkException("No service alias matching \"" + className + "\"");
            }

            return container.get(type);
        });
    }

    public <R> R evaluate(String expression)
    {
        RuntimeBag runtimeBag = RuntimeBagContext.get();
        Map<String, Object> env = MapUtils.createHashMap(entry("runtimeBag", runtimeBag), entry("$", runtimeBag));

        if (runtimeBag != null) {
            env.putAll(router.getRouteParameters());
        }

        return this.evaluate(expression, env);
    }

    public static <R> R evaluateStandalone(String expression)
    {
        return evaluateStandalone(expression, new HashMap<>());
    }

    public static <R> R evaluateStandalone(String expression, Map<String, Object> env)
    {
        ExpressionLanguage el = new ExpressionLanguage(null);

        return el.evaluate(expression, env);
    }
}
