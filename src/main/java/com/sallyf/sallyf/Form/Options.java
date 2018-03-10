package com.sallyf.sallyf.Form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Options extends HashMap<String, Object>
{
    public static final String ATTRIBUTES_KEY = "attributes";

    public static final String CONSTRAINTS_KEY = "constraints";

    public static final String LABEL_KEY = "label";

    public static final String MAPPED_KEY = "mapped";

    public Options()
    {
        super();
        initialize();
    }

    public Options(Options options)
    {
        super(options);
        initialize();
    }

    public void initialize()
    {
        setAttributes(new HashMap<>());
        setMapped(true);
        put(CONSTRAINTS_KEY, new ArrayList<ConstraintInterface>());
    }

    public Map<String, String> getAttributes()
    {
        return (Map<String, String>) get(ATTRIBUTES_KEY);
    }

    public void setAttributes(Map<String, String> attributes)
    {
        put(ATTRIBUTES_KEY, attributes);
    }

    public String getLabel()
    {
        return (String) get(LABEL_KEY);
    }

    public void setLabel(String label)
    {
        put(LABEL_KEY, label);
    }

    public void setMapped(boolean mapped)
    {
        put(MAPPED_KEY, mapped);
    }

    public boolean isMapped()
    {
        return (Boolean) get(MAPPED_KEY);
    }

    public List<ConstraintInterface> getConstraints()
    {
        return (List<ConstraintInterface>) get(CONSTRAINTS_KEY);
    }
}
