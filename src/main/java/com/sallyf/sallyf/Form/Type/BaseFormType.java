package com.sallyf.sallyf.Form.Type;

import com.sallyf.sallyf.Form.FormDataTransformer;
import com.sallyf.sallyf.Form.FormTypeInterface;
import com.sallyf.sallyf.Form.FormView;
import com.sallyf.sallyf.Form.Options;

public abstract class BaseFormType<O extends Options, VD, FD> implements FormTypeInterface<O, VD, FD>
{

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
