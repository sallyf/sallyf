package com.sallyf.sallyf.Form;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class FormView<T extends FormTypeInterface<O, ND>, O extends Options, ND>
{
    private String name;

    private Form<T, O, ND> form;

    private FormView parentView;

    private LinkedHashSet<FormView> children;

    private O vars;

    private Object data;

    public FormView(String name, Form<T, O, ND> form, FormView parentView, O vars)
    {
        this.name = name;
        this.form = form;
        this.parentView = parentView;
        this.vars = vars;
    }

    public Form<T, O, ND> getForm()
    {
        return form;
    }

    public O getVars()
    {
        return vars;
    }

    public FormView getParent()
    {
        return parentView;
    }

    public LinkedHashSet<FormView> getChildren()
    {
        return children;
    }

    public void setChildren(LinkedHashSet<FormView> children)
    {
        this.children = children;
    }

    public ErrorsBag getErrorsBag()
    {
        return form.getErrorsBag();
    }

    public Object getData()
    {
        return data;
    }

    public void setData(Object data)
    {
        this.data = data;
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

        FormView current = this;

        while (null != current) {
            names.add(0, current.getName());

            current = current.getParent();
        }

        StringBuilder sb = new StringBuilder();
        for (String name : names) {
            if(name != null) {
                if (sb.toString().isEmpty()) {
                    sb.append(name);
                } else {
                    sb.append(String.format("[%s]", name));
                }
            }
        }

        return sb.toString();
    }
}
