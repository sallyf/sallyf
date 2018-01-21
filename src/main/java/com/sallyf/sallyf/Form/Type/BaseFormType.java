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
    private Map<String, FormTypeInterface> children;

    private String name;

    private FormTypeInterface parent;

    private O options;

    public BaseFormType(String name, FormTypeInterface parent)
    {
        this.name = name;
        this.parent = parent;
        this.children = new LinkedHashMap<>();
        this.options = createOptions();
    }

    @Override
    public Map<String, FormTypeInterface> getChildren()
    {
        return children;
    }

    @Override
    public void setChildren(Map<String, FormTypeInterface> children)
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
        O options = createOptions();

        options.getAttributes().put("name", getName());

        return options;
    }

    @Override
    public void build()
    {
        resolveOptions();

        for (FormTypeInterface child : getChildren().values()) {
            child.build();
        }
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

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String getFullName()
    {
        List<String> names = new ArrayList<>();

        FormTypeInterface current = this;

        while (null != current) {
            names.add(0, current.getName());

            current = current.getParent();
        }

        StringBuilder sb = new StringBuilder();
        for (String name : names) {
            if (sb.toString().isEmpty()) {
                sb.append(name);
            } else {
                sb.append("[" + name + "]");
            }
        }

        return sb.toString();
    }

    @Override
    public FormTypeInterface getParent()
    {
        return parent;
    }
}
