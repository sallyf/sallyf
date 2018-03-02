package com.sallyf.sallyf.Form;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ServiceInterface;
import com.sallyf.sallyf.Form.Renderer.*;
import com.sallyf.sallyf.Utils.ClassUtils;

import java.util.ArrayList;
import java.util.Collection;

public class FormManager implements ServiceInterface
{
    private ArrayList<RendererInterface<?, ?>> renderers = new ArrayList<>();

    @Override
    public void initialize(Container container)
    {
        addRenderer(FormRenderer.class);
        addRenderer(SubmitRenderer.class);
        addRenderer(TextRenderer.class);
        addRenderer(PasswordRenderer.class);
        addRenderer(CheckboxRenderer.class);
        addRenderer(TextareaRenderer.class);
        addRenderer(ChoiceRenderer.class);
        addRenderer(RadioRenderer.class);
    }

    public String render(FormView formView)
    {
        for (RendererInterface renderer : renderers) {
            if (renderer.supports(formView.getForm().getBuilder().getFormType())) {
                return renderer.renderRow(formView);
            }
        }

        return renderChildren(formView);
    }

    public String renderChildren(FormView<?, ?, ?> formView)
    {
        StringBuilder s = new StringBuilder();

        Collection<FormView> values = formView.getChildren();

        for (FormView childView : values) {
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

    public ArrayList<RendererInterface<?, ?>> getRenderers()
    {
        return renderers;
    }
}
