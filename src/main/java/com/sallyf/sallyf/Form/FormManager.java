package com.sallyf.sallyf.Form;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ServiceInterface;
import com.sallyf.sallyf.Exception.FrameworkException;
import com.sallyf.sallyf.Form.Renderer.FormRenderer;
import com.sallyf.sallyf.Form.Renderer.SubmitRenderer;
import com.sallyf.sallyf.Form.Renderer.TextRenderer;
import com.sallyf.sallyf.Form.Type.FormType;
import org.eclipse.jetty.server.Request;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
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
    }

    public String render(FormTypeInterface form)
    {
        for (RendererInterface renderer : renderers) {
            if (renderer.supports(form)) {
                form.prepareRender();

                return renderer.render(form);
            }
        }

        throw new FrameworkException("Unable to render: " + form.getClass());
    }

    public String renderChildren(FormTypeInterface form)
    {
        return renderChildren(form.getChildren());
    }

    public String renderChildren(List<FormTypeInterface> children)
    {
        StringBuilder s = new StringBuilder();

        for (FormTypeInterface child : children) {
            s.append(render(child));
        }

        return s.toString();
    }

    public void addRenderer(Class<? extends RendererInterface<?>> rendererClass)
    {
        RendererInterface<?> renderer;

        try {
            renderer = rendererClass.getConstructor(FormManager.class).newInstance(this);
        } catch (InstantiationException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            try {
                renderer = rendererClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e2) {
                e.addSuppressed(e2);

                throw new FrameworkException(e);
            }
        }

        renderers.add(renderer);
    }

    public Map<String, String[]> handleRequest(Request request, FormType form)
    {
        if (!form.getOptions().getMethod().equalsIgnoreCase(request.getMethod())) {
            return null;
        }

        Map<String, String[]> data = request.getParameterMap();

        return data;
    }
}
