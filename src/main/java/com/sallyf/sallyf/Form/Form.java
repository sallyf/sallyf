package com.sallyf.sallyf.Form;

import com.sallyf.sallyf.Utils.MapUtils;
import org.eclipse.jetty.server.Request;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public class Form<T extends FormTypeInterface<O, ND>, O extends Options, ND>
{
    private String name;

    private FormBuilder<T, O, ND> formBuilder;

    private LinkedHashSet<Form> children;

    private Form parentForm;

    private O options;

    private ErrorsBag errorsBag;

    private ND data;

    public Form(String name, FormBuilder<T, O, ND> formBuilder, Form parentForm, O options)
    {
        this.name = name;
        this.formBuilder = formBuilder;
        this.parentForm = parentForm;
        this.options = options;
        this.children = new LinkedHashSet<>();

        if (parentForm == null) {
            this.errorsBag = new ErrorsBag();
        } else {
            this.errorsBag = getRoot().getErrorsBag();
        }
    }

    public LinkedHashSet<Form> getChildren()
    {
        return children;
    }

    public Form getChildren(String name)
    {
        for (Form child : children) {
            if (child.getName().equals(name)) {
                return child;
            }
        }

        return null;
    }

    public void setChildren(LinkedHashSet<Form> children)
    {
        this.children = children;
    }

    public O getOptions()
    {
        return options;
    }

    public FormBuilder<T, O, ND> getBuilder()
    {
        return formBuilder;
    }

    public Form getParent()
    {
        return parentForm;
    }

    public FormView<T, O, ND> createView()
    {
        return createView(null);
    }

    public ErrorsBag getErrorsBag()
    {
        return errorsBag;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getFullName()
    {
        List<String> names = new ArrayList<>();

        Form current = this;

        while (null != current) {
            names.add(0, current.getName());

            current = current.getParent();
        }

        StringBuilder sb = new StringBuilder();
        for (String name : names) {
            if (name != null) {
                if (sb.toString().isEmpty()) {
                    sb.append(name);
                } else {
                    sb.append(String.format("[%s]", name));
                }
            }
        }

        return sb.toString();
    }

    public T getFormType()
    {
        return getBuilder().getFormType();
    }

    public ND getData()
    {
        return data;
    }

    public Form getRoot()
    {
        Form current = this;

        while (true) {
            Form parent = current.getParent();

            if (parent == null) {
                return current;
            }

            current = parent;
        }
    }

    public void setData(ND data)
    {
        this.data = data;
    }

    private <PT extends FormTypeInterface<PO, PD>, PO extends Options, PD> FormView<T, O, ND> createView(FormView<PT, PO, PD> parentView)
    {
        O resolvedVars = getBuilder().getFormType().createOptions();

        MapUtils.deepMerge(resolvedVars, getOptions());

        FormView<T, O, ND> formView = new FormView<>(getName(), this, parentView, resolvedVars);

        formView.setData(getData());

        getFormType().buildView(formView);

        getFormType().finishView(formView);

        LinkedHashSet<FormView> childrenFormViews = new LinkedHashSet<>();

        getChildren().forEach(childForm -> childrenFormViews.add(childForm.createView(formView)));

        formView.setChildren(childrenFormViews);

        return formView;
    }

    public void submit(Request request)
    {
        ND normalizedData = getFormType().requestToNorm(this, request);

        setData(normalizedData);

        getChildren().forEach(childForm -> childForm.submit(request));
    }

    public void propagateChildData()
    {
        if (data instanceof Map) {
            Map<String, Object> mapData = (Map<String, Object>) data;

            mapData.keySet().forEach(name -> {
                Form<?, ?, Object> child = getChildren(name);

                if (child != null) {
                    child.setData(mapData.get(name));

                    child.propagateChildData();
                }
            });
        }
    }

    public Object resolveData()
    {
        return getFormType().resolveData(this);
    }
}
