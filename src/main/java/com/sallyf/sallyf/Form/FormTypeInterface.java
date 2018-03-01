package com.sallyf.sallyf.Form;

import org.eclipse.jetty.server.Request;

public interface FormTypeInterface<O extends Options, ND>
{
    default O createOptions()
    {
        return (O) new Options();
    }

    ND requestToNorm(Form<?, O, ND> form, Request request);

    default void buildView(FormView<?, O, ND> formView)
    {

    }

    default void finishView(FormView<?, O, ND> formView)
    {

    }

    default void buildForm(Form<?, O, ND> form)
    {

    }

    default void configureOptions(FormBuilder<?, O, ND> formBuilder, O options)
    {

    }

    <T extends FormTypeInterface<O, ND>> Object resolveData(Form<T, O, ND> form);
}
