package com.sallyf.sallyf.Form.Constraint;

import com.sallyf.sallyf.Form.ErrorsBag;
import com.sallyf.sallyf.Form.Form;
import com.sallyf.sallyf.Form.ValidationError;

import java.util.function.Predicate;

public class Is extends AbstractConstraint
{
    private Predicate<Object> predicate;

    public Is(Predicate<Object> predicate)
    {
        this(predicate, "`\"${value}\" is not valid`");
    }

    public Is(Predicate<Object> predicate, String message)
    {
        super(message);

        this.predicate = predicate;
    }

    @Override
    public void validate(Object value, Form<?, ?, ?> form, ErrorsBag errorsBag)
    {
        if (!predicate.test(value)) {
            errorsBag.addError(new ValidationError(getMessage(value, form)));
        }
    }
}
