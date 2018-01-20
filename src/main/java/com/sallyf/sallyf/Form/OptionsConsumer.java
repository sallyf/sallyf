package com.sallyf.sallyf.Form;

@FunctionalInterface
public interface OptionsConsumer<O extends Options>
{
    void alert(O options);
}
