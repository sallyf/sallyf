package com.sallyf.sallyf.Form.Type;

import com.sallyf.sallyf.Form.FormTypeInterface;
import com.sallyf.sallyf.Form.Options;

import java.util.Set;

public class SubmitType extends InputType<String>
{
    public SubmitType(String name, FormTypeInterface parent)
    {
        super(name, parent);
    }

    @Override
    public Options getEnforcedOptions()
    {
        Options options = super.getEnforcedOptions();
        options.getAttributes().put("type", "submit");

        return options;
    }

    @Override
    public String transform(String[] value)
    {
        return value[0];
    }

    @Override
    public String getAttributeValue()
    {
        return getValue();
    }
}
