package com.sallyf.sallyf.Form.Type;

import com.sallyf.sallyf.Form.Form;
import com.sallyf.sallyf.Form.FormView;
import com.sallyf.sallyf.Form.Options;

import java.util.Map;

public class SubmitType extends InputType<String>
{

    @Override
    public void buildForm(Form<?, Options, String> form)
    {
        super.buildForm(form);
    }

    @Override
    public void finishView(FormView<?, Options, String> formView)
    {
        super.finishView(formView);

        Options vars = formView.getVars();
        Map<String, String> attributes = vars.getAttributes();

        if (!attributes.containsKey("value") || attributes.get("value") == null || attributes.get("value").isEmpty()) {
            attributes.put("value", vars.getLabel() != null ? vars.getLabel() : formView.getName());
        }
    }

    @Override
    String getInputType()
    {
        return "submit";
    }
}
