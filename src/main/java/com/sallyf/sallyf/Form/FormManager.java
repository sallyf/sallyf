package com.sallyf.sallyf.Form;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ServiceInterface;
import com.sallyf.sallyf.Exception.FrameworkException;
import com.sallyf.sallyf.Form.Exception.UnableToValidateException;
import com.sallyf.sallyf.Form.Renderer.CheckboxRenderer;
import com.sallyf.sallyf.Form.Renderer.FormRenderer;
import com.sallyf.sallyf.Form.Renderer.SubmitRenderer;
import com.sallyf.sallyf.Form.Renderer.TextRenderer;
import com.sallyf.sallyf.Form.Type.FormType;
import com.sallyf.sallyf.Utils.ClassUtils;
import org.eclipse.jetty.server.Request;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FormManager implements ServiceInterface
{
    private ArrayList<RendererInterface<?>> renderers = new ArrayList<>();

    @Override
    public void initialize(Container container)
    {
        addRenderer(FormRenderer.class);
        addRenderer(SubmitRenderer.class);
        addRenderer(TextRenderer.class);
        addRenderer(CheckboxRenderer.class);
    }

    public String render(FormTypeInterface form)
    {
        for (RendererInterface renderer : renderers) {
            if (renderer.supports(form)) {
                return renderer.render(form);
            }
        }

        throw new FrameworkException("Unable to render: " + form.getClass());
    }

    public String renderChildren(FormTypeInterface<?, ?> form)
    {
        return renderChildren(form.getChildren().values());
    }

    public String renderChildren(Collection<FormTypeInterface> children)
    {
        StringBuilder s = new StringBuilder();

        for (FormTypeInterface child : children) {
            s.append(render(child));
        }

        return s.toString();
    }

    public void addRenderer(Class<? extends RendererInterface<?>> rendererClass)
    {
        // If failure, ignore, fallback on parameterless constructor
        RendererInterface<?> renderer = ClassUtils.newInstance(rendererClass, e -> {}, this);

        if (null == renderer) {
            renderer = ClassUtils.newInstance(rendererClass);
        }

        renderers.add(renderer);
    }

    public Map<String, String[]> handleRequest(Request request, FormType form)
    {
        if (!form.getOptions().getMethod().equalsIgnoreCase(request.getMethod())) {
            return null;
        }

        validate(form, request);

        return request.getParameterMap();
    }

    public Map<String, FormTypeInterface> getFlatParameters(FormTypeInterface<?, ?> form)
    {
        HashMap<String, FormTypeInterface> out = new HashMap<>();

        out.put(form.getFullName(), form);

        for (FormTypeInterface child : form.getChildren().values()) {
            out.putAll(getFlatParameters(child));
        }

        return out;
    }

    public void validate(FormType form, Request request)
    {
        Map<String, FormTypeInterface> formParameters = getFlatParameters(form);
        Map<String, String[]> queryParameters = request.getParameterMap();

        if (queryParameters.keySet().size() > formParameters.keySet().size()) {
            form.getErrorsBag().addError("", new ValidationError("No extra parameter allowed"));
        }

        validate(request, form, form);
    }

    private <R> void validate(Request request, FormTypeInterface<?, R> form, FormType rootForm)
    {
        String[] rawValue = request.getParameterMap().get(form.getFullName());

        R value = form.transform(rawValue);

        for (ConstraintInterface constraint : form.getOptions().getConstraints()) {
            ErrorsBagHelper errorsBagHelper = new ErrorsBagHelper(rootForm.getErrorsBag(), form.getFullName());

            try {
                constraint.validate(value, form, errorsBagHelper);
            } catch (UnableToValidateException e) {
                errorsBagHelper.addError(new ValidationError("Unable to validate " + value.getClass()));
            }
        }

        for (FormTypeInterface child : form.getChildren().values()) {
            validate(request, child, rootForm);
        }
    }
}
