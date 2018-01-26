package com.sallyf.sallyf.Form.Type;

import com.sallyf.sallyf.Form.Form;
import com.sallyf.sallyf.Form.FormDataTransformer;
import com.sallyf.sallyf.Form.FormView;
import com.sallyf.sallyf.Form.Options;

public class CheckboxType extends InputType<String, Boolean>
{

    @Override
    String getValueAttributeName()
    {
        return "checked";
    }

    @Override
    String getInputType()
    {
        return "checkbox";
    }

    @Override
    public void finishView(FormView<?, Options, String, Boolean> formView)
    {
        // Override TextInput behavior
    }

    @Override
    public void buildForm(Form<?, Options, String, Boolean> form)
    {
        super.buildForm(form);

        if (form.getData() == null) {
            form.setData(false);
        }
    }

    @Override
    public FormDataTransformer<String, Boolean> getFormDataTransformer()
    {
        return new FormDataTransformer<String, Boolean>()
        {
            @Override
            public Boolean transform(String viewData)
            {
                return viewData == null ? false : true;
            }

            @Override
            public String reverseTransform(Boolean formData)
            {
                if (formData) {
                    return "checked";
                }

                return null;
            }
        };
    }
}
