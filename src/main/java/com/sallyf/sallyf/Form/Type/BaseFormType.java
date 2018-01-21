package com.sallyf.sallyf.Form.Type;

import com.sallyf.sallyf.Exception.FrameworkException;
import com.sallyf.sallyf.Form.FormTypeInterface;
import com.sallyf.sallyf.Form.Options;
import com.sallyf.sallyf.Form.OptionsConsumer;
import com.sallyf.sallyf.Utils.DotNotationUtils;
import com.sallyf.sallyf.Utils.MapUtils;

import java.util.*;

public abstract class BaseFormType<O extends Options> implements FormTypeInterface<O>
{
    private List<FormTypeInterface> children;

    private O options;

    public BaseFormType()
    {
        children = new ArrayList<>();
        options = createOptions();
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
    public void applyOptions(OptionsConsumer<O> optionsConsumer)
    {
        if (null != optionsConsumer) {
            optionsConsumer.alert(options);
        }
    }

    @Override
    public O getOptions()
    {
        return options;
    }

    @Override
    public O getEnforcedOptions()
    {
        return createOptions();
    }

    @Override
    public void prepareRender()
    {
        resolveOptions();
    }

    private void resolveOptions()
    {
        O resolvedOptions = createOptions();

        MapUtils.deepMerge(resolvedOptions, getOptions());
        MapUtils.deepMerge(resolvedOptions, getEnforcedOptions());

        Set<String> keys = DotNotationUtils.flattenKeys(resolvedOptions, false);
        Set<String> requiredKeys = getRequiredOptions();

        Set<String> missingKeys = new HashSet<>(requiredKeys);
        missingKeys.removeAll(keys);

        if (!keys.containsAll(requiredKeys)) {
            throw new FrameworkException("Missing required options for " + getClass() + " : " + Arrays.toString(missingKeys.toArray()) + ", got: " + Arrays.toString(keys.toArray()));
        }

        options = resolvedOptions;
    }

    @Override
    public Set<String> getRequiredOptions()
    {
        HashSet<String> options = new HashSet<>();

        options.add("attributes");
        options.add("constraints");

        return options;
    }
}
