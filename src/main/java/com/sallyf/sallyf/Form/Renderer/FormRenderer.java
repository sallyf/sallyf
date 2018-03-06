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
        return form.getClass().equals(FormType.class);
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

        s += renderFormStart(formView);
        s += renderErrors(formView);
        s += manager.renderChildren(formView);
        s += renderFormEnd(formView);

        return s;
    }

    public String renderFormStart(FormView<FormType, FormType.FormOptions, ?> formView)
    {
        return "<form " + renderAttributes(formView) + ">";
    }

    public String renderFormEnd(FormView<FormType, FormType.FormOptions, ?> formView)
    {
        return "</form>";
    }

    @Override
    public String renderLabel(FormView<FormType, FormType.FormOptions, ?> formView)
    {
        return "";
    }
}
