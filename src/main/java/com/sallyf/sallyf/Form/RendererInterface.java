package com.sallyf.sallyf.Form;

public interface RendererInterface<T extends FormTypeInterface<O, ?>, O extends Options>
{
    boolean supports(FormTypeInterface form);

    String renderErrors(FormView<T, O, ?> formView);

    String renderRow(FormView<T, O, ?> formView);

    String renderWidget(FormView<T, O, ?> formView);

    String renderLabel(FormView<T, O, ?> formView);
}
