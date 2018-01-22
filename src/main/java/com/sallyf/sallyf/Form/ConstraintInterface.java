package com.sallyf.sallyf.Form;

public interface ConstraintInterface
{
    void validate(Object value, FormTypeInterface<?, ?> form, ErrorsBagHelper errorsBag);
}
