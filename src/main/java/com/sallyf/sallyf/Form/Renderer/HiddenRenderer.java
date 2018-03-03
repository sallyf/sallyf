package com.sallyf.sallyf.Form.Renderer;

import com.sallyf.sallyf.Form.FormTypeInterface;
import com.sallyf.sallyf.Form.FormView;
import com.sallyf.sallyf.Form.Options;
import com.sallyf.sallyf.Form.Type.HiddenType;
import com.sallyf.sallyf.Form.Type.TextType;

public class HiddenRenderer extends TextRenderer
{
    @Override
    public boolean supports(FormTypeInterface form)
    {
        return form instanceof HiddenType;
    }

    @Override
    public String renderRow(FormView<TextType, Options, ?> formView)
    {
        return renderWidget(formView);
    }
}
