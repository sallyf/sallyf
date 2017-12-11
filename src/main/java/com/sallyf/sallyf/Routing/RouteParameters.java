package com.sallyf.sallyf.Routing;

import java.util.HashMap;

public class RouteParameters extends HashMap<String, String>
{
    public String get(String o, String fallback)
    {
        String s = super.get(o);

        if (s == null) {
            return fallback;
        }

        return s;
    }
}
