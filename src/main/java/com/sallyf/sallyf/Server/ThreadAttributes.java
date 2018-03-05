package com.sallyf.sallyf.Server;

import java.util.HashMap;
import java.util.Map;

public class ThreadAttributes
{
    private static ThreadLocal<Map<String, Object>> threadAttrs = ThreadLocal.withInitial(HashMap::new);

    public static <T> T get(String key)
    {
        return (T) threadAttrs.get().get(key);
    }

    public static void set(String key, Object value)
    {
        threadAttrs.get().put(key, value);
    }
}
