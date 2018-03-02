package com.sallyf.sallyf.Form.Renderer;

import com.sallyf.sallyf.Form.*;
import com.sallyf.sallyf.Utils.HTMLUtils;

import java.util.Map;

import static java.util.stream.Collectors.joining;

public abstract class BaseFormRenderer<T extends FormTypeInterface<O, ?>, O extends Options> implements RendererInterface<T, O>
{
    public String renderAttributes(FormView<T, O, ?> formView)
    {
        return renderAttributes(formView.getVars().getAttributes());
    }

    public String renderAttributes(Map<String, String> attributes)
    {
        return attributes.entrySet().stream()
                .filter(e -> null != e.getKey() & null != e.getValue())
                .map(this::renderAttribute)
                .collect(joining(" "));
    }

    public String renderAttribute(Map.Entry<String, String> entry)
    {
        return renderAttribute(entry.getKey(), entry.getValue());
    }

    public String renderAttribute(String key, String value)
    {
        return String.format("%s=\"%s\"", HTMLUtils.escapeHtmlAttribute(key), HTMLUtils.escapeHtmlAttribute(value));
    }

    @Override
    public String renderErrors(FormView<T, O, ?> formView)
    {
        StringBuilder sb = new StringBuilder();

        ErrorsBag errorsBag = formView.getErrorsBag();

        if (errorsBag.hasErrors()) {
            sb.append("<ul class=\"errors\">");
            for (ValidationError error : errorsBag.getErrors()) {
                sb.append("<li class=\"error\">" + error.getMessage() + "</li>");
            }
            sb.append("</ul>");
        }

        return sb.toString();
    }

    @Override
    public String renderRow(FormView<T, O, ?> formView)
    {
        String s = "<div class=\"row\">";

        s += renderLabel(formView);
        s += renderErrors(formView);
        s += renderWidget(formView);

        s += "</div>";

        return s;
    }

    @Override
    public String renderLabel(FormView<T, O, ?> formView)
    {
        String label = formView.getVars().getLabel();

        if (label == null) {
            return "";
        }

        return "<label>" + label + "</label>";
    }
}
