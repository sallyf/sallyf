package com.sallyf.sallyf.Form.Renderer;

import com.sallyf.sallyf.Form.FormTypeInterface;
import com.sallyf.sallyf.Form.Type.PasswordType;

public class PasswordRenderer extends TextRenderer
{
    @Override
    public boolean supports(FormTypeInterface form)
    {
        return form.getClass().equals(PasswordType.class);
    }
}
