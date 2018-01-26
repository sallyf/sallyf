package com.sallyf.sallyf.Form;

import java.util.LinkedHashMap;

public class FormView<T extends FormTypeInterface<O, VD, FD>, O extends Options, VD, FD>
{
    private Form<T, O, VD, FD> form;

    private FormView parentView;

    private LinkedHashMap<String, FormView> children;

    private O vars;

    private VD data;

    public FormView(Form<T, O, VD, FD> form, FormView parentView, O vars)
    {
        this.form = form;
        this.parentView = parentView;
        this.vars = vars;
    }

    public Form<T, O, VD, FD> getForm()
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

    public LinkedHashMap<String, FormView> getChildren()
    {
        return children;
    }

    public void setChildren(LinkedHashMap<String, FormView> children)
    {
        this.children = children;
    }

    public ErrorsBag getErrorsBag()
    {
        return form.getErrorsBag();
    }

    public VD getData()
    {
        return data;
    }

    public void setData(VD data)
    {
        this.data = data;
    }
}
