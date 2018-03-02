package com.sallyf.sallyf.Form;

import java.util.HashSet;
import java.util.Set;

public class ErrorsBag
{
    private Set<ValidationError> errors = new HashSet<>();

    public void addError(ValidationError error)
    {
        errors.add(error);
    }

    public Set<ValidationError> getErrors()
    {
        return errors;
    }

    public boolean hasErrors()
    {
        return !getErrors().isEmpty();
    }
}
