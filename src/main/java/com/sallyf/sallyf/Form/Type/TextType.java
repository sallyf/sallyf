package com.sallyf.sallyf.Form.Type;

import com.sallyf.sallyf.Form.FormTypeInterface;
import com.sallyf.sallyf.Form.Options;

import java.util.Set;

public class TextType extends InputType
{
    public TextType(String name, FormTypeInterface parent)
    {
        super(name, parent);

        getOptions().getAttributes().put("value", "");
    }

    @Override
    public Options getEnforcedOptions()
    {
        Options options = super.getEnforcedOptions();
        options.getAttributes().put("type", "text");

        return options;
    }

    @Override
    public Set<String> getRequiredOptions()
    {
        Set<String> options = super.getRequiredOptions();

        options.add("attributes.value");

        return options;
    }
}
