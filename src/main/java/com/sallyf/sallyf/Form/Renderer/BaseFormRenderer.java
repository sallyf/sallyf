package com.sallyf.sallyf.Form.Renderer;

import com.sallyf.sallyf.Form.FormTypeInterface;
import com.sallyf.sallyf.Form.RendererInterface;
import com.sallyf.sallyf.Utils.HTMLUtils;

import java.util.Map;

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
                .map(this::renderAttribute)
                .collect(joining(" "));
    }

    private String renderAttribute(Map.Entry<String, String> entry)
    {
        return String.format("%s=\"%s\"", HTMLUtils.escapeHtmlAttribute(entry.getKey()), HTMLUtils.escapeHtmlAttribute(entry.getValue()));
    }
}
