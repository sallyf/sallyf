package com.sallyf.sallyf.Form.Type;

import com.sallyf.sallyf.Form.Form;
import com.sallyf.sallyf.Form.FormTypeInterface;
import com.sallyf.sallyf.Form.FormView;
import com.sallyf.sallyf.Form.Options;
import com.sallyf.sallyf.Utils.PropertyAccessor;
import org.eclipse.jetty.server.Request;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractFormType<O extends Options, ND> implements FormTypeInterface<O, ND>
{
    @Override
    public void buildView(FormView<?, O, ND> formView)
    {
        formView.getVars().getAttributes().put("name", formView.getForm().getFullName());
    }

    @Override
    public ND requestToNorm(Form<?, O, ND> form, Request request)
    {
        try {
            return (ND) getRequestFieldData(form, request).get(0);
        } catch (ArrayIndexOutOfBoundsException ignored) {
            return null;
        }
    }

    @Override
    public ND modelToNorm(Form<?, O, ND> form, Object object)
    {
        Set<Form> children = form.getMappedChildren();

        if (children.isEmpty()) {
            return (ND) object;
        } else {
            Map<String, Object> childrenData = children.stream()
                    .collect(Collectors.toMap(
                            Form::getName,
                            child -> child.getFormType().modelToNorm(child, PropertyAccessor.get(object, child.getName()))
                    ));

            return (ND) childrenData;
        }
    }

    @Override
    public Object normToModel(Form<?, O, ND> form, Object normData)
    {
        if (normData instanceof Map) {
            ((Map<String, ?>) normData).forEach((name, value) -> {
                Form child = form.getChild(name);

                if(child.getOptions().isMapped()) {
                    Object childData = child.getFormType().normToModel(child, PropertyAccessor.get(form.getModelData(), name));

                    PropertyAccessor.set(form.getModelData(), child.getName(), childData);
                }
            });
        }

        return normData;
    }

    public List<String> getRequestFieldData(Form<?, O, ND> form, Request request)
    {
        Map<String, String[]> requestData = request.getParameterMap();

        return Arrays.asList(requestData.getOrDefault(form.getFullName(), new String[0]));
    }

    @Override
    public <T extends FormTypeInterface<O, ND>> Object resolveData(Form<T, O, ND> form)
    {
        return form.getModelData();
    }

    public <T extends FormTypeInterface<?, ?>> Object childrenDataResolver(Form<T, ?, ?> form)
    {
        Map<String, Object> out = new LinkedHashMap<>();

        for (Form child : form.getChildren()) {
            out.put(child.getName(), child.getModelData());
        }

        return out;
    }
}
