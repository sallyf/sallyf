package com.sallyf.sallyf.Form.Type;

import com.sallyf.sallyf.Form.FormTypeInterface;
import com.sallyf.sallyf.Form.Options;

public class TextareaType extends BaseFormType<Options, String>
{
    public TextareaType(String name, FormTypeInterface parent)
    {
        super(name, parent);
    }

    @Override
    public String transform(String[] value)
    {
        return value[0];
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
