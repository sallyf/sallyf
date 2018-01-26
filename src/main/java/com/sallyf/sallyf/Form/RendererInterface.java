package com.sallyf.sallyf.Form;

public interface RendererInterface<T extends FormTypeInterface<O, ?, ?>, O extends Options>
{
    boolean supports(FormTypeInterface form);

    String render(FormView<T, O, ?, ?> formView);
}
