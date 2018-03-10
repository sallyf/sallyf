package com.sallyf.sallyf.Form.Type;

import com.sallyf.sallyf.Form.Form;
import com.sallyf.sallyf.Form.FormTypeInterface;
import com.sallyf.sallyf.Form.FormView;
import com.sallyf.sallyf.Form.Options;
import org.eclipse.jetty.server.Request;

import java.util.Map;
import java.util.Objects;

public abstract class CheckableType extends InputType<Boolean>
{
    @Override
    String getValueAttributeName()
    {
        return "checked";
    }

    @Override
    public void buildForm(Form<?, Options, Boolean> form)
    {
        super.buildForm(form);

        if (form.getNormData() == null) {
            form.setNormData(false);
        }
    }

    @Override
    public void finishView(FormView<?, Options, Boolean> formView)
    {
        Map<String, String> attributes = formView.getVars().getAttributes();

        if (Boolean.valueOf(String.valueOf(formView.getData()))) {
            attributes.put(getValueAttributeName(), "checked");
        } else {
            attributes.remove(getValueAttributeName());
        }
    }

    @Override
    public Boolean requestToNorm(Form<?, Options, Boolean> form, Request request)
    {
        return super.requestToNorm(form, request) != null;
    }

    @Override
    public <T extends FormTypeInterface<Options, Boolean>> Object resolveData(Form<T, Options, Boolean> form)
    {
        return form.getNormData();
    }
}
