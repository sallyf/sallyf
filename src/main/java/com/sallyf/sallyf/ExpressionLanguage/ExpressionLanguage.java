package com.sallyf.sallyf.ExpressionLanguage;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ContainerAwareInterface;
import com.sallyf.sallyf.Exception.NonExistentServiceException;
import com.sallyf.sallyf.ExpressionLanguage.Exception.EvaluationException;

import javax.script.*;

public class ExpressionLanguage implements ContainerAwareInterface
{
    private Container container;

    private ScriptEngine engine;

    private Bindings bindings = new SimpleBindings();

    public ExpressionLanguage(Container container)
    {
        this.container = container;

        ScriptEngineManager manager = new ScriptEngineManager();
        engine = manager.getEngineByName("js");
    }

    @Override
    public void initialize() throws Exception
    {
        addBinding("container", container);
        addBinding("service", (ThrowingFunction<String, ContainerAwareInterface>) className -> {
            Class<? extends ContainerAwareInterface> type = null;

            try {
                type = Class.forName(className).asSubclass(ContainerAwareInterface.class);
            } catch (ClassNotFoundException ignored) {
            }

            if (null == type) {
                type = container.findAlias(className);
            }

            if (null == type) {
                throw new Exception("No service matching \"" + className + "\"");
            }

            ContainerAwareInterface service = container.get(type);

            if (null == service) {
                throw new NonExistentServiceException(type);
            }

            return service;
        });
    }

    public <R> R evaluate(String s) throws EvaluationException
    {
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
