package com.sallyf.sallyf.Utils;

import com.sallyf.sallyf.Exception.FrameworkException;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class DotNotationUtils
{
    public static <O> O access(Object in, String path)
    {
        if (path.isEmpty()) {
            return (O) in;
        }

        String[] parts = path.split("\\.");

        Object current = in;

        for (String part : parts) {
            Object result;

            if (current instanceof Map) {
                Map<?, ?> currentMap = (Map<?, ?>) current;

                result = currentMap.get(part);
            } else if (current instanceof List) {
                List<?> currentList = (List<?>) current;

                int partInt = Integer.parseInt(part);

                result = currentList.get(partInt);
            } else {
                throw new FrameworkException("Incompatible type: " + current.getClass());
            }

            current = result;
        }

        return (O) current;
    }

    private static void flattenKeys(Set<String> out, Collection<?> collection, String prefix, boolean leafsOnly)
    {
        int i = 0;
        for (Object o : collection) {
            flattenKeys(out, o, prefix + "." + i, leafsOnly);
            i++;
        }
    }

    private static void flattenKeys(Set<String> out, Map<?, ?> map, String prefix, boolean leafsOnly)
    {
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object key = entry.getKey();

            if (!(key instanceof String)) {
                throw new FrameworkException("Incompatible key: " + key.getClass());
            }

            String keyStr = (String) key;

            flattenKeys(out, entry.getValue(), prefix + "." + keyStr, leafsOnly);
        }
    }

    private static void flattenKeys(Set<String> out, Object in, String prefix, boolean leafsOnly)
    {
        if (!leafsOnly) {
            out.add(prefix);
        }

        if (in instanceof Collection) {
            flattenKeys(out, (Collection<?>) in, prefix, leafsOnly);
        } else if (in instanceof Map) {
            flattenKeys(out, (Map<?, ?>) in, prefix, leafsOnly);
        } else {
            out.add(prefix);
        }
    }

    public static Set<String> flattenKeys(Object in, boolean leafsOnly)
    {
        Set<String> out = new HashSet<>();

        flattenKeys(out, in, "", leafsOnly);

        out = out.stream().filter(s -> !s.isEmpty()).map(s -> s.substring(1)).collect(Collectors.toSet());

        return out;
    }


    public static Map<String, Object> flatten(Object in)
    {
        Set<String> keys = flattenKeys(in, true);

        Map<String, Object> map = new HashMap<>();

        for (String key : keys) {
            map.put(key, access(in, key));
        }

        return map;
    }

    public static Map<String, Object> pack(Map<String, Object> flatMap)
    {
        Map<String, Object> structure = new HashMap<>();

        for (Map.Entry<String, Object> entry : flatMap.entrySet()) {
            String path = entry.getKey();
            Object value = entry.getValue();

            try {
                Method mClone = value.getClass().getDeclaredMethod("clone");
                mClone.setAccessible(true);
                value = mClone.invoke(value);
            } catch (Exception ignored) {
            }

            String[] parts = path.split("\\.");

            String prefix = "";

            int i = 0;
            for (String part : parts) {
                String fullPath = prefix + (prefix.isEmpty() ? "" : ".") + part;

                Object target = access(structure, fullPath);

                if (null != target) {
                    if (!(target instanceof Map)) {
                        throw new RuntimeException("Unable to set " + fullPath + ", it is already an instance of " + target.getClass());
                    }
                }

                Map<String, Object> element = (Map<String, Object>) target;

                if (null == element) {
                    Map<String, Object> parent = access(structure, prefix);

                    if (i == parts.length - 1) {
                        parent.put(part, value);
                    } else {
                        parent.put(part, new HashMap<>());
                    }
                }

                prefix = fullPath;
                i++;
            }
        }

        return structure;
    }
}
