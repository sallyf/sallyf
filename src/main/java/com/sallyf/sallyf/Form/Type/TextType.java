package com.sallyf.sallyf.Form.Type;

import com.sallyf.sallyf.Form.Options;

public class TextType extends InputType<String, String>
{
    @Override
    public Options getEnforcedOptions()
    {
        Options options = super.getEnforcedOptions();
        options.getAttributes().put("type", "text");

        return options;
    }

    @Override
    String getInputType()
    {
        return "text";
    }
}
