package com.sallyf.sallyf.Form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Options extends HashMap<String, Object>
{
    public static final String ATTRIBUTES_KEY = "attributes";

    public static final String CONSTRAINTS_KEY = "constraints";

    public Options()
    {
        put(ATTRIBUTES_KEY, new HashMap<String, Object>());
        put(CONSTRAINTS_KEY, new ArrayList<ConstraintInterface>());
    }

    public Map<String, String> getAttributes()
    {
        return (Map<String, String>) get(ATTRIBUTES_KEY);
    }

    public List<ConstraintInterface> getConstraints()
    {
        return (List<ConstraintInterface>) get(CONSTRAINTS_KEY);
    }
}
