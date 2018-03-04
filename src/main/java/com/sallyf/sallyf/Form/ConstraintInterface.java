package com.sallyf.sallyf.Form;

public interface ConstraintInterface
{
    void validate(Form<?, ?, ?> form, ErrorsBag errorsBag);
}
