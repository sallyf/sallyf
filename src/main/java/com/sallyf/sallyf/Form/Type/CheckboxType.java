package com.sallyf.sallyf.Form.Type;

import com.sallyf.sallyf.Form.FormTypeInterface;
import com.sallyf.sallyf.Form.Options;

public class CheckboxType extends InputType<Boolean>
{
    public CheckboxType(String name, FormTypeInterface parent)
    {
        super(name, parent);
    }

    @Override
    public Options getEnforcedOptions()
    {
        Options options = super.getEnforcedOptions();
        options.getAttributes().put("type", "checkbox");

        return options;
    }

    @Override
    public Boolean transform(String[] value)
    {
        return value == null ? false : true;
    }

    @Override
    String getValueAttributeName()
    {
        return "checked";
    }

    @Override
    public String getAttributeValue()
    {
        return getValue() ? "checked" : null;
    }
}
