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
    public String renderRow(FormView<FormType, FormType.FormOptions, ?> formView)
    {
        return renderWidget(formView);
    }

    @Override
    public String renderWidget(FormView<FormType, FormType.FormOptions, ?> formView)
    {
        String s = "";

        s += "<form " + renderAttributes(formView) + ">";
        s += renderErrors(formView);
        s += manager.renderChildren(formView);
        s += "</form>";

        return s;
    }

    @Override
    public String renderLabel(FormView<FormType, FormType.FormOptions, ?> formView)
    {
        return "";
    }
}
