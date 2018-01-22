package com.sallyf.sallyf.Form.Constraint;

import com.sallyf.sallyf.Form.ErrorsBagHelper;
import com.sallyf.sallyf.Form.FormTypeInterface;
import com.sallyf.sallyf.Form.ValidationError;

public class NotEmpty extends AbstractConstraint
{
    public NotEmpty()
    {
        this("This value cannot be empty");
    }

    public NotEmpty(String message)
    {
        super(message);
    }

    @Override
    public void validate(Object value, FormTypeInterface<?, ?> form, ErrorsBagHelper errorsBag)
    {
        if (value instanceof String) {
            String stringValue = (String) value;
            if (stringValue.isEmpty()) {
                errorsBag.addError(new ValidationError(getMessage(value, form)));
            }
            return;
        }

        errorsBag.addError(new ValidationError("Unable to validate"));
    }
}
