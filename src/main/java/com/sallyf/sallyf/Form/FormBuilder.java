package com.sallyf.sallyf.Form;

import com.sallyf.sallyf.Form.Type.FormType;

public class FormBuilder
{
    public static FormType create()
    {
        return create(null);
    }

    public static FormType create(OptionsConsumer optionsConsumer)
    {
        FormType form = new FormType("", null);

        form.applyOptions(optionsConsumer);

        return form;
    }
}
