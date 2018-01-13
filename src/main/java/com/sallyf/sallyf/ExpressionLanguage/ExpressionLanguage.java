package com.sallyf.sallyf.ExpressionLanguage;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ContainerAwareInterface;
import com.sallyf.sallyf.ExpressionLanguage.Exception.EvaluationException;

import javax.script.*;
import java.util.function.Function;

public class ExpressionLanguage implements ContainerAwareInterface
{
    ScriptEngine engine;

    private Container container;

    public ExpressionLanguage(Container container)
    {
        this.container = container;

        ScriptEngineManager manager = new ScriptEngineManager();
        engine = manager.getEngineByName("js");
    }

    public <R> R evaluate(String s) throws EvaluationException
    {
        Bindings bindings = new SimpleBindings();
        bindings.put("container", container);

        bindings.put("service", (Function<String, Object>) className -> {
            Class<? extends ContainerAwareInterface> type;

            try {
                type = Class.forName(className).asSubclass(ContainerAwareInterface.class);
            } catch (ClassNotFoundException e) {
                return null;
            }

            return container.get(type);
        });

        try {
            return (R) engine.eval(s, bindings);
        } catch (ScriptException e) {
            throw new EvaluationException(e);
        }
    }
}
