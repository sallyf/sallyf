package com.sallyf.sallyf.Form.Type;

import com.sallyf.sallyf.Form.Options;

import java.util.Map;
import java.util.Set;

public class SubmitType extends InputType
{
    @Override
    public Options getEnforcedOptions()
    {
        Options options = super.getEnforcedOptions();
        options.getAttributes().put("type", "submit");

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
