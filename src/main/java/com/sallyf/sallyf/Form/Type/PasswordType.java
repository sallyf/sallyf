package com.sallyf.sallyf.Form.Type;

import com.sallyf.sallyf.Form.FormView;
import com.sallyf.sallyf.Form.Options;

public class PasswordType extends TextType
{
    @Override
    String getInputType()
    {
        return "password";
    }

    @Override
    public void finishView(FormView<?, Options, String> formView)
    {
        super.finishView(formView);

        if (formView.getForm().isSubmitted()) {
            formView.getVars().getAttributes().put("value", "");
        }
    }
}
