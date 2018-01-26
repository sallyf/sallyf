package com.sallyf.sallyf.Form.Type;

import com.sallyf.sallyf.Form.FormView;
import com.sallyf.sallyf.Form.Options;

import java.util.Set;

public class FormType extends BaseFormType<FormType.FormOptions, Object, Object>
{
    public class FormOptions extends Options
    {
        public static final String METHOD_KEY = "method";

        public FormOptions()
        {
            super();

            put(METHOD_KEY, "post");
        }

        public String getMethod()
        {
            return (String) get(METHOD_KEY);
        }

        public void setMethod(String method)
        {
            put(METHOD_KEY, method);
        }
    }

    @Override
    public FormType.FormOptions createOptions()
    {
        return new FormOptions();
    }

    @Override
    public Set<String> getRequiredOptions()
    {
        Set<String> options = super.getRequiredOptions();

        options.add("method");

        return options;
    }

    @Override
    public void buildView(FormView<?, FormOptions, Object, Object> formView)
    {
        super.buildView(formView);

        formView.getVars().getAttributes().put("method", formView.getForm().getOptions().getMethod());
    }

    @Override
    public void finishView(FormView<?, FormOptions, Object, Object> formView)
    {
        super.finishView(formView);

        formView.getVars().getAttributes().remove("name");
    }
}
