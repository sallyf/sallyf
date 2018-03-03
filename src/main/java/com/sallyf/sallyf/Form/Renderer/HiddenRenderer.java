package com.sallyf.sallyf.Form.Renderer;

import com.sallyf.sallyf.Form.FormTypeInterface;
import com.sallyf.sallyf.Form.Type.HiddenType;

public class HiddenRenderer extends TextRenderer
{
    @Override
    public boolean supports(FormTypeInterface form)
    {
        return form instanceof HiddenType;
    }
}
