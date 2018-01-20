package com.sallyf.sallyf.Form.Type;

import com.sallyf.sallyf.Exception.FrameworkException;
import com.sallyf.sallyf.Form.FormTypeInterface;
import com.sallyf.sallyf.Form.Options;
import com.sallyf.sallyf.Form.OptionsConsumer;

import java.util.Set;

public class FormType extends BaseFormType<FormType.FormOptions>
{
    public class FormOptions extends Options
    {
        public static final String METHOD_KEY = "method";

        public FormOptions()
        {
            super();

            put(METHOD_KEY, "post");

        }

        public String getMethod()
        {
            return (String) get(METHOD_KEY);
        }

        public void setMethod(String method)
        {
            put(METHOD_KEY, method);
        }
    }

    @Override
    public FormType.FormOptions createOptions()
    {
        return new FormOptions();
    }

    @Override
    public Set<String> getRequiredOptions()
    {
        Set<String> options = super.getRequiredOptions();

        options.add("method");

        return options;
    }

    @Override
    public FormType.FormOptions getEnforcedOptions()
    {
        FormType.FormOptions options = super.getEnforcedOptions();
        options.getAttributes().put("method", getOptions().getMethod());

        return options;
    }

    public FormType add(Class<? extends FormTypeInterface> childClass)
    {
        return add(childClass, null);
    }

    public FormType add(Class<? extends FormTypeInterface> childClass, OptionsConsumer optionsConsumer)
    {
        FormTypeInterface child;

        try {
            child = childClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new FrameworkException(e);
        }

        child.applyOptions(optionsConsumer);

        getChildren().add(child);

        return this;
    }
}
