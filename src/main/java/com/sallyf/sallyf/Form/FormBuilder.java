package com.sallyf.sallyf.Form;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ServiceInterface;
import com.sallyf.sallyf.Utils.ClassUtils;
import com.sallyf.sallyf.Utils.MapUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FormBuilder<T extends FormTypeInterface<O, VD, FD>, O extends Options, VD, FD>
{
    private Container container;

    private String name;

    private T formType;

    private LinkedHashMap<String, FormBuilder> children;

    private FormBuilder<T, O, VD, FD> parent = null;

    private O options;

    private FD data;

    public FormBuilder(Container container, String name, Class<T> formTypeClass, FormBuilder parent)
    {
        this(container, name, formTypeClass);

        this.parent = parent;
    }

    public FormBuilder(Container container, String name, Class<T> formTypeClass)
    {
        this.container = container;
        this.name = name;
        this.children = new LinkedHashMap<>();

        boolean isService = ServiceInterface.class.isAssignableFrom(formTypeClass);

        if (isService && container.has(formTypeClass)) {
            this.formType = (T) container.get(formTypeClass.asSubclass(ServiceInterface.class));
        } else {
            this.formType = ClassUtils.newInstance(formTypeClass);
        }

        this.options = this.formType.createOptions();
    }

    public <CT extends FormTypeInterface<CO, CVD, CFD>, CO extends Options, CVD, CFD> FormBuilder<T, O, VD, FD> add(String name, Class<CT> childTypeClass, OptionsConsumer<CO> optionsConsumer)
    {
        FormBuilder<CT, CO, CVD, CFD> childBuilder = new FormBuilder<>(container, name, childTypeClass, this);

        getChildren().put(name, childBuilder);

        if (optionsConsumer != null) {
            optionsConsumer.apply(childBuilder.getOptions());
        }

        return this;
    }

    public <CT extends FormTypeInterface<CO, CVD, CFD>, CO extends Options, CVD, CFD> FormBuilder<T, O, VD, FD> add(String name, Class<CT> childTypeClass)
    {
        return add(name, childTypeClass, null);
    }

    public T getFormType()
    {
        return formType;
    }

    public LinkedHashMap<String, FormBuilder> getChildren()
    {
        return children;
    }

    public O getOptions()
    {
        return options;
    }

    public FormBuilder<T, O, VD, FD> getParent()
    {
        return parent;
    }

    public Form<T, O, VD, FD> getForm()
    {
        return getForm(null);
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

        FormBuilder current = this;

        while (null != current) {
            names.add(0, current.getName());

            current = current.getParent();
        }

        StringBuilder sb = new StringBuilder();
        for (String name : names) {
            if (sb.toString().isEmpty()) {
                sb.append(name);
            } else {
                sb.append(String.format("[%s]", name));
            }
        }

        return sb.toString();
    }

    private <PT extends FormTypeInterface<PO, PVD, PFD>, PO extends Options, PVD, PFD> Form<T, O, VD, FD> getForm(Form<PT, PO, PVD, PFD> parentForm)
    {
        propagateChildData();

        O resolvedOptions = getFormType().createOptions();

        MapUtils.deepMerge(resolvedOptions, getOptions());

        getFormType().configureOptions(this, resolvedOptions);

        Form<T, O, VD, FD> form = new Form<>(this, parentForm, resolvedOptions);

        form.setRawData(data);

        getFormType().buildForm(form);

        LinkedHashMap<String, Form> childrenForms = new LinkedHashMap<>();

        getChildren().forEach((key, value) -> childrenForms.put(key, value.getForm(form)));

        form.setChildren(childrenForms);

        return form;
    }

    private void propagateChildData()
    {
        if (data instanceof Map) {
            Map<String, Object> mapData = (Map<String, Object>) data;

            mapData.keySet().forEach(k -> {
                FormBuilder child = getChildren().get(k);

                if (child != null) {
                    child.setData(mapData.get(k));
                }
            });
        }
    }

    public void setData(FD data)
    {
        this.data = data;
    }

    public FD getData()
    {
        return data;
    }
}
