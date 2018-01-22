package com.sallyf.sallyf.Form.Renderer;

import com.sallyf.sallyf.Form.FormTypeInterface;
import com.sallyf.sallyf.Form.Type.TextType;

public class TextRenderer extends InputRenderer<TextType>
{
    @Override
    public boolean supports(FormTypeInterface form)
    {
        return form instanceof TextType;
    }
}
