package com.sallyf.sallyf.Form.Type;

import java.util.Set;

public abstract class InputType extends BaseFormType
{
    @Override
    public Set<String> getRequiredOptions()
    {
        Set<String> options = super.getRequiredOptions();

        options.add("attributes.type");

        return options;
    }
}
