package com.sallyf.sallyf.Form;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ServiceInterface;
import com.sallyf.sallyf.Exception.FrameworkException;
import com.sallyf.sallyf.Form.Exception.UnableToValidateException;
import com.sallyf.sallyf.Form.Renderer.*;
import com.sallyf.sallyf.Form.Type.FormType;
import com.sallyf.sallyf.Utils.ClassUtils;
import com.sallyf.sallyf.Utils.RequestUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.util.IO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class FormManager implements ServiceInterface
{
    private ArrayList<RendererInterface<?, ?>> renderers = new ArrayList<>();

    @Override
    public void initialize(Container container)
    {
        addRenderer(FormRenderer.class);
        addRenderer(SubmitRenderer.class);
        addRenderer(TextRenderer.class);
        addRenderer(CheckboxRenderer.class);
        addRenderer(TextareaRenderer.class);
    }

    public String render(FormView formView)
    {
        for (RendererInterface renderer : renderers) {
            if (renderer.supports(formView.getForm().getBuilder().getFormType())) {
                return renderer.render(formView);
            }
        }

        throw new FrameworkException("Unable to render: " + formView.getClass());
    }

    public String renderChildren(FormView<?, ?, ?, ?> formView)
    {
        StringBuilder s = new StringBuilder();

        for (FormView childView : formView.getChildren().values()) {
            s.append(render(childView));
        }

        return s.toString();
    }

    public void addRenderer(Class<? extends RendererInterface<?, ?>> rendererClass)
    {
        // If failure, ignore, fallback on parameterless constructor
        RendererInterface<?, ?> renderer = ClassUtils.newInstance(rendererClass, e -> {}, this);

        if (null == renderer) {
            renderer = ClassUtils.newInstance(rendererClass);
        }

        renderers.add(renderer);
    }

    public Map<String, Object> handleRequest(Request request, Form<FormType, FormType.FormOptions, Object, Object> form)
    {
        if (!form.getOptions().getMethod().equalsIgnoreCase(request.getMethod())) {
            return null;
        }

        try {
            String s = IO.toString(request.getInputStream());
            Map<String, Object> data = RequestUtils.parseQuery(s, true);
            form.setRawData(data);

            validate(form);

            return data;
        } catch (IOException e) {
            throw new FrameworkException(e);
        }
    }

    private <FD> void validate(Form<?, ?, ?, FD> form)
    {
        for (ConstraintInterface constraint : form.getOptions().getConstraints()) {
            ErrorsBagHelper errorsBagHelper = new ErrorsBagHelper(form.getErrorsBag(), form.getFullName());

            try {
                constraint.validate(form.getData(), form, errorsBagHelper);
            } catch (UnableToValidateException e) {
                errorsBagHelper.addError(new ValidationError(String.format("Unable to validate %s for constraint %s", form.getData(), constraint)));
            }
        }

        for (Form child : form.getChildren().values()) {
            validate(child);
        }
    }
}
