package com.sallyf.sallyf.Form.Constraint;

import com.sallyf.sallyf.Form.ErrorsBag;
import com.sallyf.sallyf.Form.Form;
import com.sallyf.sallyf.Form.ValidationError;

import java.util.function.Predicate;

public class Is extends AbstractConstraint
{
    private Predicate<Form<?, ?, ?>> predicate;

    public Is(Predicate<Form<?, ?, ?>> predicate)
    {
        this(predicate, "`\"${value}\" is not valid`");
    }

    public Is(Predicate<Form<?, ?, ?>> predicate, String message)
    {
        super(message);

        this.predicate = predicate;
    }

    @Override
    public void validate(Form<?, ?, ?> form, ErrorsBag errorsBag)
    {
        if (!predicate.test(form)) {
            errorsBag.addError(new ValidationError(getMessage(form.resolveData(), form)));
        }
    }
}
