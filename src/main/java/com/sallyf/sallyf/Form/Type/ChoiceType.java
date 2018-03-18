package com.sallyf.sallyf.Form.Type;

import com.sallyf.sallyf.Form.*;
import org.eclipse.jetty.server.Request;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChoiceType extends AbstractFormType<ChoiceType.ChoiceOptions, Object>
{
    public class ChoiceOptions extends Options
    {
        public static final String CHOICES_KEY = "choices";

        public static final String EXPANDED_KEY = "expanded";

        public static final String MULTIPLE_KEY = "multiple";

        public static final String CHOICE_VALUE_RESOLVER_KEY = "choice_value_resolver";

        public static final String CHOICE_LABEL_RESOLVER_KEY = "choice_label_resolver";

        public static final String CHOICE_OPTIONS_CONSUMER = "choice_options_consumer";

        @Override
        public void initialize()
        {
            super.initialize();
            put(CHOICES_KEY, new LinkedHashSet<>());
            put(EXPANDED_KEY, null);
            put(MULTIPLE_KEY, null);
            put(CHOICE_VALUE_RESOLVER_KEY, (Function<Object, String>) Objects::toString);
            put(CHOICE_LABEL_RESOLVER_KEY, (Function<Object, String>) Objects::toString);
            put(CHOICE_OPTIONS_CONSUMER, (OptionsConsumer<Options>) (options) -> {});
        }

        public LinkedHashSet<?> getChoices()
        {
            return (LinkedHashSet<?>) get(CHOICES_KEY);
        }

        public void setChoices(LinkedHashSet<?> set)
        {
            put(CHOICES_KEY, set);
        }

        public boolean isExpanded()
        {
            return (boolean) get(EXPANDED_KEY);
        }

        public void setExpanded(boolean expanded)
        {
            put(EXPANDED_KEY, expanded);
        }

        public boolean isMultiple()
        {
            return (boolean) get(MULTIPLE_KEY);
        }

        public void setMultiple(boolean multiple)
        {
            put(MULTIPLE_KEY, multiple);
        }

        public Function<Object, String> getChoiceValueResolver()
        {
            return (Function<Object, String>) get(CHOICE_VALUE_RESOLVER_KEY);
        }

        public void setChoiceValueResolver(Function<Object, String> resolver)
        {
            put(CHOICE_VALUE_RESOLVER_KEY, resolver);
        }

        public Function<Object, String> getChoiceLabelResolver()
        {
            return (Function<Object, String>) get(CHOICE_LABEL_RESOLVER_KEY);
        }

        public void setChoiceLabelResolver(Function<Object, String> resolver)
        {
            put(CHOICE_LABEL_RESOLVER_KEY, resolver);
        }

        public OptionsConsumer<Options> getChoiceOptionsConsumer()
        {
            return (OptionsConsumer<Options>) get(CHOICE_OPTIONS_CONSUMER);
        }

        public void setChoiceOptionsConsumer(OptionsConsumer<Options> resolver)
        {
            put(CHOICE_OPTIONS_CONSUMER, resolver);
        }
    }

    @Override
    public ChoiceType.ChoiceOptions createOptions()
    {
        return new ChoiceOptions();
    }

    @Override
    public void finishView(FormView<?, ChoiceOptions, Object> formView)
    {
        super.finishView(formView);

        boolean isExpanded = formView.getVars().isExpanded();
        boolean isMultiple = formView.getVars().isMultiple();

        Map<String, String> attributes = formView.getVars().getAttributes();

        if (!isExpanded && isMultiple) {
            attributes.put("multiple", "multiple");
        }
    }

    @Override
    public void buildForm(Form<?, ChoiceOptions, Object> form)
    {
        super.buildForm(form);

        ChoiceOptions options = form.getOptions();

        Map<String, Object> valuesChoices = options.getChoices()
                .stream()
                .collect(Collectors.toMap(c -> options.getChoiceValueResolver().apply(c), c -> c));

        options.put("values_choices", valuesChoices);

        boolean isExpanded = options.isExpanded();
        boolean isMultiple = options.isMultiple();

        if (isExpanded) {
            Class<? extends CheckableType> type = isMultiple ? CheckboxType.class : RadioType.class;

            int i = 0;
            for (Object choice : options.getChoices()) {
                form.getBuilder().add(String.valueOf(i++), type, (childBuilder, childOptions) -> {
                    childOptions.put("choice", choice);

                    String value = options.getChoiceValueResolver().apply(choice);

                    childOptions.put("value", value);
                    childOptions.setLabel(null);

                    childOptions.getAttributes().put("value", value);

                    options.getChoiceOptionsConsumer().apply(childOptions);

                    childBuilder.setData(choice.equals(form.getNormData()));
                });
            }
        }
    }

    @Override
    public <T extends FormTypeInterface<ChoiceOptions, Object>> Object resolveData(Form<T, ChoiceOptions, Object> form)
    {
        ChoiceOptions options = form.getOptions();

        boolean isMultiple = options.isMultiple();
        boolean isExpanded = options.isExpanded();

        if (isExpanded) {
            if (isMultiple) {
                return form.getChildren()
                        .stream()
                        .filter(child -> Boolean.valueOf(String.valueOf(child.getModelData())))
                        .map(child -> child.getOptions().get("choice"))
                        .collect(Collectors.toList());
            } else {
                for (Form child : form.getChildren()) {
                    Object choice = child.getOptions().get("choice");
                    if (Boolean.valueOf(String.valueOf(child.getModelData()))) {
                        return choice;
                    }
                }

                return null;
            }
        } else {
            return form.getModelData();
        }
    }

    @Override
    public Object requestToNorm(Form<?, ChoiceOptions, Object> form, Request request)
    {
        ChoiceOptions options = form.getOptions();

        Map<String, Object> valuesChoices = (Map<String, Object>) options.get("values_choices");

        boolean isMultiple = options.isMultiple();

        if (isMultiple) {
            return getRequestFieldData(form, request).stream().map(valuesChoices::get).collect(Collectors.toList());
        } else {
            return valuesChoices.get(super.requestToNorm(form, request));
        }
    }
}
