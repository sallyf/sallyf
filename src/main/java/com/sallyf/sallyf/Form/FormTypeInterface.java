package com.sallyf.sallyf.Form;

import java.util.Set;

public interface FormTypeInterface<O extends Options, VD, FD>
{
    default O createOptions()
    {
        return (O) new Options();
    }

    default O getEnforcedOptions()
    {
        return null;
    }

    default Set<String> getRequiredOptions()
    {
        return null;
    }

    FormDataTransformer<VD, FD> getFormDataTransformer();

    default void buildView(FormView<?, O, VD, FD> formView)
    {

    }

    default void finishView(FormView<?, O, VD, FD> formView)
    {

    }

    default void buildForm(Form<?, O, VD, FD> form)
    {

    }

    default void configureOptions(FormBuilder<?, O, VD, FD> formBuilder, O options)
    {

    }
}
