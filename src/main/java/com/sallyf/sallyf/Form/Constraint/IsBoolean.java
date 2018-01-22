package com.sallyf.sallyf.Form.Constraint;

import com.sallyf.sallyf.Form.Exception.UnableToValidateException;

public class IsBoolean extends Is
{
    public IsBoolean(boolean expected)
    {
        this(expected, "\"{{value}}\" is not " + Boolean.toString(expected));
    }

    public IsBoolean(boolean expected, String message)
    {
        super(
                (value) -> {
                    if (value instanceof String) {
                        return value.equals(Boolean.toString(expected));
                    }

                    if (value instanceof Boolean) {
                        boolean booleanValue = (Boolean) value;

                        return booleanValue == expected;
                    }

                    throw new UnableToValidateException();
                },
                message
        );
    }
}
