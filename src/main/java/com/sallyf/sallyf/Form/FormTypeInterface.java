package com.sallyf.sallyf.Form;

import java.util.List;
import java.util.Set;

public interface FormTypeInterface<O extends Options>
{
    default O createOptions()
    {
        return (O) new Options();
    }

    List<FormTypeInterface> getChildren();

    void setChildren(List<FormTypeInterface> children);

    void applyOptions(OptionsConsumer<O> optionsConsumer);

    O getOptions();

    O getEnforcedOptions();

    void prepareRender();

    Set<String> getRequiredOptions();
}
