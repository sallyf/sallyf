package com.sallyf.sallyf.Form.Constraint;

import com.sallyf.sallyf.Form.ErrorsBag;
import com.sallyf.sallyf.Form.Exception.UnableToValidateException;
import com.sallyf.sallyf.Form.Form;
import com.sallyf.sallyf.Form.ValidationError;

public class NotEmpty extends AbstractConstraint
{
    public NotEmpty()
    {
        this("`This field cannot be empty`");
    }

    public NotEmpty(String message)
    {
        super(message);
    }

    @Override
    public void validate(Object value, Form<?, ?, ?> form, ErrorsBag errorsBag)
    {
        if(value == null) {
            errorsBag.addError(new ValidationError(getMessage(value, form)));
            return;
        }

        if (value instanceof String) {
            String stringValue = (String) value;
            if (stringValue.isEmpty()) {
                errorsBag.addError(new ValidationError(getMessage(value, form)));
            }
            return;
        }

        throw new UnableToValidateException();
    }
}
