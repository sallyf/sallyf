package com.sallyf.sallyf.Form.Type;

import com.sallyf.sallyf.Exception.FrameworkException;
import com.sallyf.sallyf.Form.FormTypeInterface;
import com.sallyf.sallyf.Form.Options;
import com.sallyf.sallyf.Form.OptionsAlterer;
import com.sallyf.sallyf.Utils.DotNotationUtils;
import com.sallyf.sallyf.Utils.MapUtils;

import java.util.*;

public abstract class BaseFormType implements FormTypeInterface
{
    private List<FormTypeInterface> children;

    private Options options;

    public BaseFormType()
    {
        children = new ArrayList<>();
        options = new Options();
    }

    @Override
    public List<FormTypeInterface> getChildren()
    {
        return children;
    }

    @Override
    public void setChildren(List<FormTypeInterface> children)
    {
        this.children = children;
    }

    @Override
    public void applyOptions(OptionsAlterer optionsAlterer)
    {
        if (null != optionsAlterer) {
            optionsAlterer.alert(options);
        }
    }

    @Override
    public Options getOptions()
    {
        return options;
    }

    @Override
    public Options getEnforcedOptions()
    {
        return new Options();
    }

    @Override
    public void prepareRender()
    {
        resolveOptions();
    }

    private void resolveOptions()
    {
        Options resolvedOptions = new Options();

        MapUtils.deepMerge(resolvedOptions, getOptions());
        MapUtils.deepMerge(resolvedOptions, getEnforcedOptions());

        Set<String> keys = DotNotationUtils.flattenKeys(resolvedOptions, true);
        Set<String> requiredKeys = getRequiredOptions();

        Set<String> missingKeys = new HashSet<>(requiredKeys);
        missingKeys.removeAll(keys);

        if (!keys.containsAll(requiredKeys)) {
            throw new FrameworkException("Missing required options for " + getClass() + " : " + Arrays.toString(missingKeys.toArray()));
        }

        options = resolvedOptions;
    }

    @Override
    public Set<String> getRequiredOptions()
    {
        return new HashSet<>();
    }
}
