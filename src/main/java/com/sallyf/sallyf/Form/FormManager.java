package com.sallyf.sallyf.Form;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ServiceInterface;
import com.sallyf.sallyf.Exception.FrameworkException;
import com.sallyf.sallyf.Form.Renderer.FormRenderer;
import com.sallyf.sallyf.Form.Renderer.SubmitRenderer;
import com.sallyf.sallyf.Form.Renderer.TextRenderer;
import com.sallyf.sallyf.Form.Type.FormType;
import com.sallyf.sallyf.Utils.DotNotationUtils;
import com.sallyf.sallyf.Utils.RequestUtils;
import org.eclipse.jetty.server.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public String getRequestContent(Request request) {
        try {
            return new BufferedReader(new InputStreamReader(request.getInputStream())).lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new FrameworkException(e);
        }
    }

    public Map<String, Object> handleRequest(Request request, FormType form)
    {
        if (!form.getOptions().getMethod().equalsIgnoreCase(request.getMethod())) {
            return null;
        }

        return RequestUtils.parseQuery(getRequestContent(request), true);
    }

    public void validate(FormTypeInterface form, Request request)
    {
        Map<String, String[]> data = request.getParameterMap();
    }
}
