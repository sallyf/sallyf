package com.sallyf.sallyf.Form.Constraint;

import com.sallyf.sallyf.ExpressionLanguage.ExpressionLanguage;
import com.sallyf.sallyf.Form.ConstraintInterface;
import com.sallyf.sallyf.Form.Form;
import com.sallyf.sallyf.Form.FormTypeInterface;

import javax.script.Bindings;
import javax.script.SimpleBindings;

public abstract class AbstractConstraint implements ConstraintInterface
{
    private String messageTemplate;

    public AbstractConstraint(String messageTemplate)
    {
        this.messageTemplate = messageTemplate;
    }

    public String getMessage(Object value, Form form)
    {
        Bindings bindings = new SimpleBindings();
        bindings.put("value", value);
        bindings.put("form", form);

        return ExpressionLanguage.mustacheEvaluate(messageTemplate, bindings);
    }
}
