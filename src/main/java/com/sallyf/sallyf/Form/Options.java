package com.sallyf.sallyf.Form;

import java.util.HashMap;
import java.util.Map;

public class Options extends HashMap<String, Object>
{
    public static final String ATTRIBUTES_KEY = "attributes";

    public Options()
    {
        put(ATTRIBUTES_KEY, new HashMap<String, Object>());
    }

    public Map<String, String> getAttributes()
    {
        return (Map<String, String>) get(ATTRIBUTES_KEY);
    }
}
