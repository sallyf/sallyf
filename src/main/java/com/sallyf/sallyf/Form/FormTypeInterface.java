package com.sallyf.sallyf.Form;

import java.util.List;
import java.util.Set;

public interface FormTypeInterface
{
    List<FormTypeInterface> getChildren();

    void setChildren(List<FormTypeInterface> children);

    void applyOptions(OptionsAlterer optionsAlterer);

    Options getOptions();

    Options getEnforcedOptions();

    void resolveOptions();

    Set<String> getRequiredOptions();
}
