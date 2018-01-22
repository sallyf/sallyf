package com.sallyf.sallyf.Form.Renderer;

import com.sallyf.sallyf.Form.FormTypeInterface;
import com.sallyf.sallyf.Form.Type.TextareaType;

public class TextareaRenderer extends BaseFormRenderer<TextareaType>
{
    @Override
    public boolean supports(FormTypeInterface form)
    {
        return form instanceof TextareaType;
    }

    @Override
    public String render(TextareaType form)
    {
        String s = "";

        s += "<textarea " + renderAttributes(form) + ">";
        s += form.getValue();
        s += "</textarea>";

        return s;
    }
}
