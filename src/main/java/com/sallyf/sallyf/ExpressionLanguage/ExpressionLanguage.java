package com.sallyf.sallyf.ExpressionLanguage;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ServiceInterface;
import com.sallyf.sallyf.Exception.FrameworkException;
import com.sallyf.sallyf.ExpressionLanguage.Exception.EvaluationException;
import com.sallyf.sallyf.ExpressionLanguage.Libraries.Mustache;
import com.sallyf.sallyf.Router.Router;
import com.sallyf.sallyf.Server.RuntimeBag;

import javax.script.*;
import java.util.function.Function;

public class ExpressionLanguage implements ServiceInterface
{
    private Router router;

    private static ScriptEngine engine = makeEngine();

    private Bindings bindings = new SimpleBindings();

    public ExpressionLanguage(Router router)
    {
        this.router = router;
    }

    private static ScriptEngine makeEngine()
    {
        ScriptEngineManager manager = new ScriptEngineManager();

        return manager.getEngineByName("js");
    }

    @Override
    public void initialize(Container container)
    {
        addBinding("container", container);
        addBinding("service", (Function<String, ServiceInterface>) className -> {
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

    public static <R> R evaluatePure(String s)
    {
        return evaluatePure(s, new SimpleBindings());
    }

    public static <R> R evaluatePure(String s, Bindings bindings)
    {
        try {
            return (R) engine.eval(s, bindings);
        } catch (ScriptException e) {
            throw new EvaluationException(e);
        }
    }

    public static <R> R mustacheEvaluate(String s, Bindings bindings)
    {
        String s1 = "Mustache.render(mustacheTemplate, view)";

        String mustache = Mustache.get();

        bindings.put("mustacheTemplate", s);
        bindings.put("view", bindings);

        return evaluatePure(mustache + s1, bindings);
    }

    public <R> R evaluate(String s)
    {
        return evaluate(s, new SimpleBindings());
    }

    public <R> R evaluate(String s, RuntimeBag runtimeBag)
    {
        Bindings bindings = new SimpleBindings();
        bindings.put("$", runtimeBag);

        if (null != runtimeBag) {
            bindings.putAll(router.getRouteParameters(runtimeBag));
        }

        return evaluate(s, bindings);
    }

    public <R> R evaluate(String s, Bindings extraBindings)
    {
        Bindings bindings = new SimpleBindings();
        bindings.putAll(this.bindings);
        bindings.putAll(extraBindings);

        return evaluatePure(s, bindings);
    }

    public void addBinding(String name, Object value)
    {
        bindings.put(name, value);
    }
}
