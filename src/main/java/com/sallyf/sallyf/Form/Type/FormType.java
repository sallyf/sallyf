package com.sallyf.sallyf.Form.Type;

import com.sallyf.sallyf.Form.Form;
import com.sallyf.sallyf.Form.FormTypeInterface;
import com.sallyf.sallyf.Form.FormView;
import com.sallyf.sallyf.Form.Options;

import java.util.LinkedHashMap;
import java.util.Map;

public class FormType extends AbstractFormType<FormType.FormOptions, Object>
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
    public void buildView(FormView<?, FormOptions, Object> formView)
    {
        super.buildView(formView);

        formView.getVars().getAttributes().put("method", formView.getForm().getOptions().getMethod());
    }

    @Override
    public void finishView(FormView<?, FormOptions, Object> formView)
    {
        super.finishView(formView);

        formView.getVars().getAttributes().remove("name");
    }

    @Override
    public <T extends FormTypeInterface<FormOptions, Object>> Object resolveData(Form<T, FormOptions, Object> form)
    {
        Map<String, Object> out = new LinkedHashMap<>();

        for (Form child : form.getChildren()) {
            out.put(child.getName(), child.resolveData());
        }

        return out;
    }
}
