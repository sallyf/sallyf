package com.sallyf.sallyf.Form;

import com.sallyf.sallyf.Utils.MapUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public class Form<T extends FormTypeInterface<O, VD, FD>, O extends Options, VD, FD>
{
    private FormBuilder<T, O, VD, FD> formBuilder;

    private LinkedHashMap<String, Form> children;

    private Form parentForm;

    private O options;

    private ErrorsBag errorsBag;

    private FD data;

    public Form(FormBuilder<T, O, VD, FD> formBuilder, Form parentForm, O options)
    {
        this.formBuilder = formBuilder;
        this.parentForm = parentForm;
        this.options = options;
        this.children = new LinkedHashMap<>();

        if (parentForm == null) {
            this.errorsBag = new ErrorsBag();
        } else {
            this.errorsBag = getRoot().getErrorsBag();
        }
    }

    public LinkedHashMap<String, Form> getChildren()
    {
        return children;
    }

    public void setChildren(LinkedHashMap<String, Form> children)
    {
        this.children = children;
    }

    public O getOptions()
    {
        return options;
    }

    public FormBuilder<T, O, VD, FD> getBuilder()
    {
        return formBuilder;
    }

    public Form getParent()
    {
        return parentForm;
    }

    public FormView<T, O, VD, FD> createView()
    {
        return createView(null);
    }

    public ErrorsBag getErrorsBag()
    {
        return errorsBag;
    }

    public String getName()
    {
        return getBuilder().getName();
    }

    public void setName(String name)
    {
        getBuilder().setName(name);
    }

    public String getFullName()
    {
        return getBuilder().getFullName();
    }

    public T getFormType()
    {
        return getBuilder().getFormType();
    }

    public FD getData()
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

    public void setData(FD data)
    {
        this.data = data;
    }

    public void setRawData(Object rawData, boolean transform)
    {
        if (rawData instanceof Map) {
            Map<String, Object> rawMap = (Map<String, Object>) rawData;

            getChildren().forEach((name, child) -> {
                String fullName = child.getFullName();

                if (rawMap.containsKey(fullName)) {
                    child.setRawData(rawMap.get(child.getFullName()), transform);
                }
            });
        } else {
            if (transform) {
                setData(getFormType().getFormDataTransformer().transform((VD) rawData));
            } else {
                setData((FD) rawData);
            }
        }
    }

    private <PT extends FormTypeInterface<PO, PVD, PFD>, PO extends Options, PVD, PFD> FormView<T, O, VD, FD> createView(FormView<PT, PO, PVD, PFD> parentView)
    {
        O resolvedVars = getBuilder().getFormType().createOptions();

        MapUtils.deepMerge(resolvedVars, getOptions());

        FormView<T, O, VD, FD> formView = new FormView<>(this, parentView, resolvedVars);

        VD viewData = getFormType().getFormDataTransformer().reverseTransform(getData());

        formView.setData(viewData);

        getFormType().buildView(formView);

        getFormType().finishView(formView);

        LinkedHashMap<String, FormView> childrenFormViews = new LinkedHashMap<>();

        getChildren().forEach((key, value) -> childrenFormViews.put(key, value.createView(formView)));

        formView.setChildren(childrenFormViews);

        return formView;
    }
}
