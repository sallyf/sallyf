package com.sallyf.sallyf.Form.Renderer;

import com.sallyf.sallyf.Form.FormManager;
import com.sallyf.sallyf.Form.FormTypeInterface;
import com.sallyf.sallyf.Form.FormView;
import com.sallyf.sallyf.Form.Options;
import com.sallyf.sallyf.Form.Type.ChoiceType;
import com.sallyf.sallyf.Utils.MapUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.sallyf.sallyf.Utils.MapUtils.entry;

public class ChoiceRenderer extends BaseFormRenderer<ChoiceType, ChoiceType.ChoiceOptions>
{
    private FormManager manager;

    public ChoiceRenderer(FormManager manager)
    {
        this.manager = manager;
    }

    @Override
    public boolean supports(FormTypeInterface form)
    {
        return form.getClass().equals(ChoiceType.class);
    }

    @Override
    public String renderWidget(FormView<ChoiceType, ChoiceType.ChoiceOptions, ?> form)
    {
        ChoiceType.ChoiceOptions vars = form.getVars();

        boolean isExpanded = vars.isExpanded();
        boolean isMultiple = vars.isMultiple();

        if (isExpanded) {
            if (isMultiple) {
                return renderCheckboxes(form);
            } else {
                return renderRadio(form);
            }
        } else {
            return renderSelect(form);
        }
    }

    public String renderSelect(FormView<ChoiceType, ChoiceType.ChoiceOptions, ?> form)
    {
        ChoiceType.ChoiceOptions vars = form.getVars();

        String s = "";

        s += "<select " + renderAttributes(form) + ">";

        for (Object choice : vars.getChoices()) {
            final String value = vars.getChoiceValueResolver().apply(choice);

            Options childOptions = new Options();

            vars.getChoiceOptionsConsumer().apply(childOptions);

            Map<String, String> entryAttributes = childOptions.getAttributes();

            entryAttributes.put("value", value);

            if (Objects.equals(form.getData(), choice)) {
                entryAttributes.put("selected", "selected");
            }

            s += "<option " + renderAttributes(entryAttributes) + ">" + vars.getChoiceLabelResolver().apply(choice) + "</option>";
        }

        s += "</select>";

        return s;
    }

    public String renderRadio(FormView<ChoiceType, ChoiceType.ChoiceOptions, ?> form)
    {
        return renderInputs(form);
    }

    public String renderCheckboxes(FormView<ChoiceType, ChoiceType.ChoiceOptions, ?> form)
    {
        return renderInputs(form);
    }

    public String renderInputs(FormView<ChoiceType, ChoiceType.ChoiceOptions, ?> form)
    {
        String s = "";

        String label = form.getVars().getLabel();

        s += "<div>";
        if (label != null) {
            s += "<label>" + label + "</label>";
        }
        s += manager.renderChildren(form);
        s += "</div>";

        return s;
    }
}
