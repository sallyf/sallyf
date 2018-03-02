package com.sallyf.sallyf.Form;

public interface ConstraintInterface
{
    void validate(Object value, Form<?, ?, ?> form, ErrorsBag errorsBag);
}
