package com.sallyf.sallyf.Form;

import java.util.Map;
import java.util.Set;

public interface FormTypeInterface<O extends Options, R>
{
    default O createOptions()
    {
        return (O) new Options();
    }

    Map<String, FormTypeInterface> getChildren();

    void setChildren(Map<String, FormTypeInterface> children);

    void applyOptions(OptionsConsumer<O> optionsConsumer);

    O getOptions();

    O getEnforcedOptions();

    void build();

    Set<String> getRequiredOptions();

    String getName();

    void setName(String name);

    String getFullName();

    FormTypeInterface getParent();

    R transform(String[] value);
}
