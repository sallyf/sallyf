package com.sallyf.sallyf.Form.Renderer;

import com.sallyf.sallyf.Form.FormTypeInterface;
import com.sallyf.sallyf.Form.FormView;
import com.sallyf.sallyf.Form.Options;
import com.sallyf.sallyf.Form.Type.TextareaType;

public class TextareaRenderer extends BaseFormRenderer<TextareaType, Options>
{
    @Override
    public boolean supports(FormTypeInterface form)
    {
        return form instanceof TextareaType;
    }

    @Override
    public String render(FormView<TextareaType, Options, ?, ?> formView)
    {
        String s = "";

        s += "<textarea " + renderAttributes(formView) + ">";
        s += formView.getData();
        s += "</textarea>";

        return s;
    }
}
