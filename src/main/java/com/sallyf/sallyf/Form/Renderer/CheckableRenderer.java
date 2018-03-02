package com.sallyf.sallyf.Form.Renderer;

import com.sallyf.sallyf.Form.FormTypeInterface;
import com.sallyf.sallyf.Form.FormView;
import com.sallyf.sallyf.Form.Options;

public abstract class CheckableRenderer<T extends FormTypeInterface<O, ?>, O extends Options> extends InputRenderer<T, O>
{
    @Override
    public String renderRow(FormView<T, O, ?> formView)
    {
        String label = formView.getVars().getLabel();

        if (label == null || label.isEmpty()) {
            label = "";
        }

        String s = "<div class=\"row\">";
        s += renderErrors(formView);
        s += "<label>";
        s += renderWidget(formView);
        s += " " + label;
        s += "</label>";

        s += "</div>";

        return s;
    }

    @Override
    public String renderLabel(FormView<T, O, ?> formView)
    {
        throw new RuntimeException("Not implemented");
    }
}
