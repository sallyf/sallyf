package com.sallyf.sallyf.Form.Renderer;

import com.sallyf.sallyf.Form.*;
import com.sallyf.sallyf.Utils.HTMLUtils;

import java.util.Map;
import java.util.Set;

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

    public String renderErrors(FormView<T, O, ?> formView)
    {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, Set<ValidationError>> entry : formView.getErrorsBag().getErrors().entrySet()) {
            String name = entry.getKey();
            Set<ValidationError> errors = entry.getValue();

            sb.append("<div>");
            sb.append("<p>" + name + ":</p>");
            sb.append("<ul>");
            for (ValidationError error : errors) {
                sb.append("<li>" + error.getMessage() + "</li>");
            }
            sb.append("</ul>");
            sb.append("</div>");
        }

        return sb.toString();
    }
}
