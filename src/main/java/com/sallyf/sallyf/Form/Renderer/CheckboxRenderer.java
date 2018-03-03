package com.sallyf.sallyf.Form.Renderer;

import com.sallyf.sallyf.Form.FormTypeInterface;
import com.sallyf.sallyf.Form.Options;
import com.sallyf.sallyf.Form.Type.CheckboxType;

public class CheckboxRenderer extends CheckableRenderer<CheckboxType, Options>
{
    @Override
    public boolean supports(FormTypeInterface form)
    {
        return form.getClass().equals(CheckboxType.class);
    }
}
