package com.sallyf.sallyf.Form.Type;

import com.sallyf.sallyf.Form.Options;

import java.util.Set;

public class TextType extends InputType
{
    @Override
    public Options getEnforcedOptions()
    {
        Options options = super.getEnforcedOptions();
        options.getAttributes().put("type", "text");
        options.getAttributes().put("value", "");

        return options;
    }

    @Override
    public Set<String> getRequiredOptions()
    {
        Set<String> options = super.getRequiredOptions();

        options.add("attributes.value");
        options.add("attributes.name");

        return options;
    }
}
