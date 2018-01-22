package com.sallyf.sallyf.Form;

import java.util.Set;

public class ErrorsBagHelper
{
    private ErrorsBag errorsBag;

    private String name;

    public ErrorsBagHelper(ErrorsBag errorsBag, String name)
    {
        this.errorsBag = errorsBag;
        this.name = name;
    }

    public void addError(ValidationError error)
    {
        errorsBag.addError(name, error);
    }

    public Set<ValidationError> getErrors()
    {
        return errorsBag.getError(name);
    }

    public boolean hasErrors()
    {
        return !getErrors().isEmpty();
    }
}
