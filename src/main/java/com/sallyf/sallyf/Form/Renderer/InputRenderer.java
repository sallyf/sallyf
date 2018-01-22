package com.sallyf.sallyf.Form.Renderer;

import com.sallyf.sallyf.Form.FormTypeInterface;

public abstract class InputRenderer<T extends FormTypeInterface> extends BaseFormRenderer<T>
{
    @Override
    public String render(T form)
    {
        return "<input " + renderAttributes(form) + ">";
    }
}
