package com.sallyf.sallyf.Form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Options extends HashMap<String, Object>
{
    public static final String ATTRIBUTES_KEY = "attributes";

    public static final String CONSTRAINTS_KEY = "constraints";

    public static final String DATA_TYPE_KEY = "data_type";

    public Options()
    {
        initialize();
    }

    public Options(Options options)
    {
        super(options);
        initialize();
    }

    public void initialize()
    {
        put(ATTRIBUTES_KEY, new HashMap<String, Object>());
        put(CONSTRAINTS_KEY, new ArrayList<ConstraintInterface>());
        put(DATA_TYPE_KEY, null);
    }

    public Options newInstance()
    {
        return new Options();
    }

    public Map<String, String> getAttributes()
    {
        return (Map<String, String>) get(ATTRIBUTES_KEY);
    }

    public void setAttributes(Map<String, String> attributes)
    {
        put(ATTRIBUTES_KEY, attributes);
    }

    public List<ConstraintInterface> getConstraints()
    {
        return (List<ConstraintInterface>) get(CONSTRAINTS_KEY);
    }
}
