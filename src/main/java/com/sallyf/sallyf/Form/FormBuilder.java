package com.sallyf.sallyf.Form;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ServiceInterface;
import com.sallyf.sallyf.Utils.ClassUtils;
import com.sallyf.sallyf.Utils.MapUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class FormBuilder<T extends FormTypeInterface<O, ND>, O extends Options, ND>
{
    private Container container;

    private String name;

    private T formType;

    private LinkedHashSet<FormBuilder> children;

    private FormBuilder<T, O, ND> parent = null;

    private O options;

    private ND data;

    public FormBuilder(Container container, String name, Class<T> formTypeClass, FormBuilder parent)
    {
        this(container, name, formTypeClass);

        this.parent = parent;
    }

    public FormBuilder(Container container, String name, Class<T> formTypeClass)
    {
        this.container = container;
        this.name = name;
        this.children = new LinkedHashSet<>();

        boolean isService = ServiceInterface.class.isAssignableFrom(formTypeClass);

        if (isService && container.has(formTypeClass)) {
            this.formType = (T) container.get(formTypeClass.asSubclass(ServiceInterface.class));
        } else {
            this.formType = ClassUtils.newInstance(formTypeClass);
        }

        this.options = this.formType.createOptions();
    }

    public <CT extends FormTypeInterface<CO, CD>, CO extends Options, CD> FormBuilder<T, O, ND> add(String name, Class<CT> childTypeClass, BiConsumer<FormBuilder<CT, CO, CD>, CO> childBuilderConsumer)
    {
        FormBuilder<CT, CO, CD> childBuilder = new FormBuilder<>(container, name, childTypeClass, this);

        getChildren().add(childBuilder);

        childBuilderConsumer.accept(childBuilder, childBuilder.getOptions());

        return this;
    }

    public <CT extends FormTypeInterface<CO, CD>, CO extends Options, CD> FormBuilder<T, O, ND> add(String name, Class<CT> childTypeClass, OptionsConsumer<CO> optionsConsumer)
    {
        return add(name, childTypeClass, (childBuilder, options) -> optionsConsumer.apply(options));
    }

    public <CT extends FormTypeInterface<CO, CD>, CO extends Options, CD> FormBuilder<T, O, ND> add(String name, Class<CT> childTypeClass)
    {
        return add(name, childTypeClass, (e1, e2) -> {});
    }

    public T getFormType()
    {
        return formType;
    }

    public LinkedHashSet<FormBuilder> getChildren()
    {
        return children;
    }

    public FormBuilder getChildren(String name)
    {
        for (FormBuilder child : children) {
            if (child.getName().equals(name)) {
                return child;
            }
        }

        return null;
    }

    public O getOptions()
    {
        return options;
    }

    public FormBuilder<T, O, ND> getParent()
    {
        return parent;
    }

    public Form<T, O, ND> getForm()
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

    private <PT extends FormTypeInterface<PO, PD>, PO extends Options, PD> Form<T, O, ND> getForm(Form<PT, PO, PD> parentForm)
    {
        O resolvedOptions = getFormType().createOptions();

        MapUtils.deepMerge(resolvedOptions, getOptions());

//        getFormType().configureOptions(this, resolvedOptions);

        Form<T, O, ND> form = new Form<>(getName(), this, parentForm, resolvedOptions);

        form.setData(getData());

        getFormType().buildForm(form);

        LinkedHashSet<Form> childrenForms = new LinkedHashSet<>();

        getChildren().forEach(childrenFormBuilder -> childrenForms.add(childrenFormBuilder.getForm(form)));

        form.setChildren(childrenForms);

        form.propagateChildData();

        return form;
    }

    public void setData(ND data)
    {
        this.data = data;
    }

    public ND getData()
    {
        return data;
    }
}
