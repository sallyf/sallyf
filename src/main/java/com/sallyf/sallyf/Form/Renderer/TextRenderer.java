package com.sallyf.sallyf.Form.Renderer;

import com.sallyf.sallyf.Form.FormTypeInterface;
import com.sallyf.sallyf.Form.Type.TextType;

public class TextRenderer extends BaseFormRenderer<TextType>
{
    @Override
    public boolean supports(FormTypeInterface form)
    {
        return form instanceof TextType;
    }

    @Override
    public String render(TextType form)
    {
        return "<input " + renderAttributes(form) + ">";
    }
}
