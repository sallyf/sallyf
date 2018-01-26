package com.sallyf.sallyf.Form;

public interface FormDataTransformer<VD, FD>
{
    FD transform(VD viewData);

    VD reverseTransform(FD formData);
}
