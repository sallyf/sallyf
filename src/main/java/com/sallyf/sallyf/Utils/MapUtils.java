package com.sallyf.sallyf.Utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapUtils
{
    // https://stackoverflow.com/a/29698326/3212099
    public static Map deepMerge(Map original, Map newMap)
    {
        for (Object key : newMap.keySet()) {
            if (newMap.get(key) instanceof Map && original.get(key) instanceof Map) {
                Map originalChild = (Map) original.get(key);
                Map newChild = (Map) newMap.get(key);
                original.put(key, deepMerge(originalChild, newChild));
            } else if (newMap.get(key) instanceof List && original.get(key) instanceof List) {
                List originalChild = (List) original.get(key);
                List newChild = (List) newMap.get(key);
                for (Object each : newChild) {
                    if (!originalChild.contains(each)) {
                        originalChild.add(each);
                    }
                }
            } else {
                original.put(key, newMap.get(key));
            }
        }
        return original;
    }

    public static Map<String, Object> parse(String in)
    {
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();

        return new Gson().fromJson(in, mapType);
    }

    public static <K, V> Map.Entry<K, V> entry(K key, V value)
    {
        return new AbstractMap.SimpleEntry<>(key, value);
    }

    public static <M extends Map<K, V>, K, V> Collector<Map.Entry<K, V>, ?, M> entriesToMap(Class<M> mapClass)
    {
        return Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (oldValue, newValue) -> oldValue,
                () -> ClassUtils.newInstance(mapClass)
        );
    }

    public static <M extends Map<K, V>, K, V> M createMap(Class<M> mapClass, Map.Entry<K, V>... entries)
    {
        return Stream.of(entries).collect(entriesToMap(mapClass));
    }

    @SafeVarargs
    public static <K, V> HashMap<K, V> createHashMap(Map.Entry<K, V>... entries)
    {
        return createMap(HashMap.class, entries);
    }

    @SafeVarargs
    public static <K, V> LinkedHashMap<K, V> createLinkedHashMap(Map.Entry<K, V>... entries)
    {
        return createMap(LinkedHashMap.class, entries);
    }
}
