package com.sallyf.sallyf.Form.Type;

import com.sallyf.sallyf.Form.FormDataTransformer;
import com.sallyf.sallyf.Form.FormTypeInterface;
import com.sallyf.sallyf.Form.FormView;
import com.sallyf.sallyf.Form.Options;

import java.util.HashSet;
import java.util.Set;

public abstract class BaseFormType<O extends Options, VD, FD> implements FormTypeInterface<O, VD, FD>
{
    @Override
    public Set<String> getRequiredOptions()
    {
        HashSet<String> options = new HashSet<>();

        options.add("attributes");
        options.add("constraints");

        return options;
    }

    @Override
    public void buildView(FormView<?, O, VD, FD> formView)
    {
        formView.getVars().getAttributes().put("name", formView.getForm().getFullName());
    }

    @Override
    public FormDataTransformer<VD, FD> getFormDataTransformer()
    {
        return new FormDataTransformer<VD, FD>()
        {
            @Override
            public FD transform(VD viewData)
            {
                return (FD) viewData;
            }

            @Override
            public VD reverseTransform(FD formData)
            {
                return (VD) formData;
            }
        };
    }
}
