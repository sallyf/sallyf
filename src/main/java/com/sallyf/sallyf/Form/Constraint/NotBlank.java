package com.sallyf.sallyf.Form.Constraint;

import com.sallyf.sallyf.Form.ConstraintInterface;
import com.sallyf.sallyf.Form.ErrorsBag;
import com.sallyf.sallyf.Form.FormTypeInterface;
import com.sallyf.sallyf.Form.ValidationError;

public class NotBlank implements ConstraintInterface
{
    @Override
    public void validate(Object value, FormTypeInterface<?> form, ErrorsBag errorsBag)
    {
        if (value instanceof Object[]) {
            Object[] arrayValue = (Object[]) value;

            if (value instanceof String[]) {
                String[] arrayString = (String[]) arrayValue;

                if (arrayString.length == 1) {
                    if (arrayString[0].isEmpty()) {
                        errorsBag.addError(form.getFullName(), new ValidationError("This value cannot be blank"));
                    }
                    return;
                }
            }
        }

        errorsBag.addError(form.getFullName(), new ValidationError("Unable to validate"));
    }
}
