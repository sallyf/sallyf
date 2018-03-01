package com.sallyf.sallyf.Form.Renderer;

import com.sallyf.sallyf.Form.FormManager;
import com.sallyf.sallyf.Form.FormTypeInterface;
import com.sallyf.sallyf.Form.FormView;
import com.sallyf.sallyf.Form.Type.FormType;

public class FormRenderer extends BaseFormRenderer<FormType, FormType.FormOptions>
{
    private FormManager manager;

    public FormRenderer(FormManager manager)
    {
        this.manager = manager;
    }

    @Override
    public boolean supports(FormTypeInterface form)
    {
        return form instanceof FormType;
    }

    @Override
    public String render(FormView<FormType, FormType.FormOptions, ?> form)
    {
        String s = "";

        s += "<form " + renderAttributes(form) + ">";
        s += renderErrors(form);
        s += manager.renderChildren(form);
        s += "</form>";

        return s;
    }
}
