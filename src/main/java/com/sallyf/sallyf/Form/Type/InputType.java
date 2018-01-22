package com.sallyf.sallyf.Form.Type;

import com.sallyf.sallyf.Form.FormTypeInterface;
import com.sallyf.sallyf.Form.Options;

import java.util.Set;

public abstract class InputType<R> extends BaseFormType<Options, R>
{
    public InputType(String name, FormTypeInterface parent)
    {
        super(name, parent);
    }

    @Override
    public Set<String> getRequiredOptions()
    {
        Set<String> options = super.getRequiredOptions();

        options.add("attributes.type");
        options.add("attributes." + getValueAttributeName());

        return options;
    }

    @Override
    public void applyValue()
    {
        getOptions().getAttributes().put(getValueAttributeName(), getAttributeValue());
    }

    String getValueAttributeName()
    {
        return "value";
    }
}
