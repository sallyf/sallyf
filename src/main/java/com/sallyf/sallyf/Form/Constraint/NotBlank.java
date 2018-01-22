package com.sallyf.sallyf.Form.Constraint;

import com.sallyf.sallyf.Form.ErrorsBagHelper;
import com.sallyf.sallyf.Form.FormTypeInterface;
import com.sallyf.sallyf.Form.ValidationError;

public class NotBlank extends AbstractConstraint
{
    public NotBlank()
    {
        this("This value cannot be blank");
    }

    public NotBlank(String message)
    {
        super(message);
    }

    @Override
    public void validate(Object value, FormTypeInterface<?> form, ErrorsBagHelper errorsBag)
    {
        if (value instanceof Object[]) {
            Object[] arrayValue = (Object[]) value;

            if (value instanceof String[]) {
                String[] arrayString = (String[]) arrayValue;

                if (arrayString.length == 1) {
                    if (arrayString[0].isEmpty()) {
                        errorsBag.addError(new ValidationError(getMessage(value, form)));
                    }
                    return;
                }
            }
        }

        errorsBag.addError(new ValidationError("Unable to validate"));
    }
}
