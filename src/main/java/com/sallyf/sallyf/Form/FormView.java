package com.sallyf.sallyf.Form;

import java.util.LinkedHashSet;

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

    public FormView getChild(String name)
    {
        for (FormView child : children) {
            if (child.getName().equals(name)) {
                return child;
            }
        }

        return null;
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
        return getForm().getFullName();
    }
}
