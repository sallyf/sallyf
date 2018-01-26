package com.sallyf.sallyf.Form.Renderer;

import com.sallyf.sallyf.Form.FormTypeInterface;
import com.sallyf.sallyf.Form.Options;
import com.sallyf.sallyf.Form.Type.TextType;

public class TextRenderer extends InputRenderer<TextType, Options>
{
    @Override
    public boolean supports(FormTypeInterface form)
    {
        return form instanceof TextType;
    }
}
