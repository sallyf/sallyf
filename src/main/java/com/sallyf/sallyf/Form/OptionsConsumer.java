package com.sallyf.sallyf.Form;

@FunctionalInterface
public interface OptionsConsumer<O extends Options>
{
    void apply(O options);
}
