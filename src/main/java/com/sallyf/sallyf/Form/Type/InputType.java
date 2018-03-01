package com.sallyf.sallyf.Form.Type;

import com.sallyf.sallyf.Form.FormView;
import com.sallyf.sallyf.Form.Options;
import com.sallyf.sallyf.Utils.DataUtils;
import com.sallyf.sallyf.Utils.MapUtils;

import java.util.HashMap;
import java.util.Map;

public abstract class InputType<ND> extends AbstractFormType<Options, ND>
{
    String getValueAttributeName()
    {
        return "value";
    }

    abstract String getInputType();

    @Override
    public void buildView(FormView<?, Options, ND> formView)
    {
        super.buildView(formView);

        Map<String, String> attributes = formView.getVars().getAttributes();

        attributes.put("type", getInputType());
    }

    @Override
    public void finishView(FormView<?, Options, ND> formView)
    {
        super.finishView(formView);

        Map<String, String> attributes = formView.getVars().getAttributes();

        Map<String, String> newAttributes = new HashMap<>();
        newAttributes.put(getValueAttributeName(), DataUtils.fallback((String) formView.getData(), ""));

        MapUtils.deepMerge(newAttributes, attributes);

        formView.getVars().setAttributes(newAttributes);
    }
}
