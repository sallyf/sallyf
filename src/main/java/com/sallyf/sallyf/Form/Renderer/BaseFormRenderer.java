package com.sallyf.sallyf.Form.Renderer;

import com.sallyf.sallyf.Form.FormTypeInterface;
import com.sallyf.sallyf.Form.RendererInterface;
import com.sallyf.sallyf.Form.Type.FormType;
import com.sallyf.sallyf.Form.ValidationError;
import com.sallyf.sallyf.Utils.HTMLUtils;

import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.joining;

public abstract class BaseFormRenderer<T extends FormTypeInterface> implements RendererInterface<T>
{
    public String renderAttributes(FormTypeInterface form)
    {
        return renderAttributes(form.getOptions().getAttributes());
    }

    public String renderAttributes(Map<String, String> attributes)
    {
        return attributes.entrySet().stream()
                .filter(e -> null != e.getKey() & null != e.getValue())
                .map(this::renderAttribute)
                .collect(joining(" "));
    }

    private String renderAttribute(Map.Entry<String, String> entry)
    {
        return String.format("%s=\"%s\"", HTMLUtils.escapeHtmlAttribute(entry.getKey()), HTMLUtils.escapeHtmlAttribute(entry.getValue()));
    }

    public String renderErrors(FormType form)
    {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, Set<ValidationError>> entry : form.getErrorsBag().getErrors().entrySet()) {
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
