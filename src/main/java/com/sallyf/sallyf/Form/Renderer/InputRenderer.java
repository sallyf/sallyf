package com.sallyf.sallyf.Form.Renderer;

import com.sallyf.sallyf.Form.FormTypeInterface;
import com.sallyf.sallyf.Form.FormView;
import com.sallyf.sallyf.Form.Options;

public abstract class InputRenderer<T extends FormTypeInterface<O, ?>, O extends Options> extends BaseFormRenderer<T, O>
{
    @Override
    public String renderWidget(FormView<T, O, ?> formView)
    {
        return "<input " + renderAttributes(formView) + ">";
    }
}
