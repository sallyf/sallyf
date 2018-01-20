package com.sallyf.sallyf.Form.Type;

import com.sallyf.sallyf.Exception.FrameworkException;
import com.sallyf.sallyf.Form.FormTypeInterface;
import com.sallyf.sallyf.Form.Options;
import com.sallyf.sallyf.Form.OptionsAlterer;

import java.util.Set;

public class FormType extends BaseFormType
{
    @Override
    public Set<String> getRequiredOptions()
    {
        Set<String> options = super.getRequiredOptions();

        options.add("attributes.method");

        return options;
    }

    @Override
    public Options getEnforcedOptions()
    {
        Options options = super.getEnforcedOptions();
        options.getAttributes().put("method", "post");

        return options;
    }

    public FormType add(Class<? extends FormTypeInterface> childClass)
    {
        return add(childClass, null);
    }

    public FormType add(Class<? extends FormTypeInterface> childClass, OptionsAlterer optionsAlterer)
    {
        FormTypeInterface child;

        try {
            child = childClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new FrameworkException(e);
        }

        child.applyOptions(optionsAlterer);

        getChildren().add(child);

        return this;
    }
}
