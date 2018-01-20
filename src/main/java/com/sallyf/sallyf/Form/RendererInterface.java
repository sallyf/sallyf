package com.sallyf.sallyf.Form;

public interface RendererInterface<T extends FormTypeInterface>
{
    boolean supports(FormTypeInterface form);

    String render(T form);
}
