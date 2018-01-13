package com.sallyf.sallyf.ExpressionLanguage;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ContainerAwareInterface;
import com.sallyf.sallyf.Exception.FrameworkException;
import com.sallyf.sallyf.ExpressionLanguage.Exception.EvaluationException;
import com.sallyf.sallyf.Router.RouteParameters;
import com.sallyf.sallyf.Router.Router;
import com.sallyf.sallyf.Server.RuntimeBag;

import javax.script.*;
import java.util.Map;
import java.util.function.Function;

public class ExpressionLanguage implements ContainerAwareInterface
{
    private Container container;

    private Router router;

    private ScriptEngine engine;

    private Bindings bindings = new SimpleBindings();

    public ExpressionLanguage(Container container, Router router)
    {
        this.container = container;
        this.router = router;

        ScriptEngineManager manager = new ScriptEngineManager();
        engine = manager.getEngineByName("js");
    }

    @Override
    public void initialize()
    {
        addBinding("container", container);
        addBinding("service", (Function<String, ContainerAwareInterface>) className -> {
            Class<? extends ContainerAwareInterface> type = null;

            try {
                type = Class.forName(className).asSubclass(ContainerAwareInterface.class);
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

    public <R> R evaluate(String s)
    {
        return evaluate(s, null);
    }

    public <R> R evaluate(String s, RuntimeBag runtimeBag)
    {
        Bindings bindings = new SimpleBindings();
        bindings.putAll(this.bindings);
        bindings.put("$", runtimeBag);

        if (null != runtimeBag) {
            RouteParameters routeParameters = router.getRouteParameters(runtimeBag);

            for (Map.Entry<String, Object> entry : routeParameters.entrySet()) {
                bindings.put(entry.getKey(), entry.getValue());
            }
        }

        try {
            return (R) engine.eval(s, bindings);
        } catch (ScriptException e) {
            throw new EvaluationException(e);
        }
    }

    public void addBinding(String name, Object value)
    {
        bindings.put(name, value);
    }
}
