package com.raphaelvigee.sally.Router;

import java.util.HashMap;

public class RouteParameters extends HashMap<String, Object>
{
    public <R> R get(String o, R fallback)
    {
        R s = (R) super.get(o);

        if (s == null) {
            return fallback;
        }

        return s;
    }
}
