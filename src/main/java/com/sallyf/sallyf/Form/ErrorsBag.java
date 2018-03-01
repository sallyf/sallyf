package com.sallyf.sallyf.Form;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ErrorsBag
{
    private HashMap<String, Set<ValidationError>> errors = new HashMap<>();

    public void addError(String path, ValidationError error)
    {
        if (!errors.containsKey(path)) {
            errors.put(path, new HashSet<>());
        }

        errors.get(path).add(error);
    }

    public HashMap<String, Set<ValidationError>> getErrors()
    {
        return errors;
    }

    public Set<ValidationError> getError(String path)
    {
        return errors.get(path);
    }

    public boolean hasErrors()
    {
        return !getErrors().isEmpty();
    }
}
