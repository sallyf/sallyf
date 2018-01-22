package com.sallyf.sallyf.Form.Type;

import com.sallyf.sallyf.Form.ErrorsBag;
import com.sallyf.sallyf.Form.FormTypeInterface;
import com.sallyf.sallyf.Form.Options;
import com.sallyf.sallyf.Form.OptionsConsumer;
import com.sallyf.sallyf.Utils.ClassUtils;

import java.util.Set;
import java.util.function.Supplier;

public class FormType extends BaseFormType<FormType.FormOptions, Object>
{
    private ErrorsBag errorsBag;

    public FormType(String name, FormTypeInterface parent)
    {
        super(name, parent);

        errorsBag = new ErrorsBag();
    }

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

    public <O extends Options, R> FormType add(String name, Class<? extends FormTypeInterface<O, R>> childClass, Supplier<R> supplier)
    {
        return add(name, childClass, null, supplier);
    }

    public <O extends Options, R> FormType add(String name, Class<? extends FormTypeInterface<O, R>> childClass, OptionsConsumer<O> optionsConsumer, Supplier<R> supplier)
    {
        add(name, childClass, optionsConsumer);

        if (null != supplier) {
            getChildren().get(name).setValue(supplier.get());
        }

        return this;
    }

    public FormType add(String name, Class<? extends FormTypeInterface> childClass)
    {
        return add(name, childClass, null);
    }

    public FormType add(String name, Class<? extends FormTypeInterface> childClass, OptionsConsumer optionsConsumer)
    {
        FormTypeInterface child = ClassUtils.newInstance(childClass, name, this);

        child.applyOptions(optionsConsumer);

        getChildren().put(name, child);

        return this;
    }

    public ErrorsBag getErrorsBag()
    {
        return errorsBag;
    }

    @Override
    public Object transform(String[] value)
    {
        return value;
    }

    @Override
    public String getAttributeValue()
    {
        return null;
    }

    @Override
    public void applyValue()
    {

    }
}
