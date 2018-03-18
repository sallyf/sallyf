package com.sallyf.sallyf.Form.Type;

import com.sallyf.sallyf.Form.Form;
import com.sallyf.sallyf.Form.FormTypeInterface;
import com.sallyf.sallyf.Form.FormView;
import com.sallyf.sallyf.Form.Options;
import org.eclipse.jetty.server.Request;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class AbstractFormType<O extends Options, ND> implements FormTypeInterface<O, ND>
{
    @Override
    public void buildView(FormView<?, O, ND> formView)
    {
        O vars = formView.getVars();
        Map<String, String> attributes = vars.getAttributes();
        attributes.put("name", formView.getForm().getFullName());

        if (vars.isDisabled()) {
            attributes.put("disabled", "disabled");
        }
    }

    @Override
    public ND requestToNorm(Form<?, O, ND> form, Request request)
    {
        Map<String, String[]> requestData = request.getParameterMap();

        List<String> fieldData = Arrays.asList(requestData.getOrDefault(form.getFullName(), new String[0]));

        try {
            return (ND) fieldData.get(0);
        } catch (ArrayIndexOutOfBoundsException ignored) {
            return null;
        }
    }

    @Override
    public <T extends FormTypeInterface<O, ND>> Object resolveData(Form<T, O, ND> form)
    {
        return form.getData();
    }
}
