package com.sallyf.sallyf.Form.Constraint;

import com.sallyf.sallyf.ExpressionLanguage.ExpressionLanguage;
import com.sallyf.sallyf.Form.ConstraintInterface;
import com.sallyf.sallyf.Form.Form;
import com.sallyf.sallyf.Form.FormTypeInterface;
import com.sallyf.sallyf.Utils.MapUtils;

import javax.script.Bindings;
import javax.script.SimpleBindings;
import java.util.HashMap;

import static com.sallyf.sallyf.Utils.MapUtils.entry;

public abstract class AbstractConstraint implements ConstraintInterface
{
    private String messageTemplate;

    public AbstractConstraint(String messageTemplate)
    {
        this.messageTemplate = messageTemplate;
    }

    public String getMessage(Object value, Form form)
    {
        HashMap<String, Object> env = MapUtils.createHashMap(entry("value", value), entry("form", form));

        return ExpressionLanguage.evaluateStandalone(messageTemplate, env);
    }
}
