package com.sallyf.sallyf.Server;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RuntimeStorage
{
    private HashMap<Object, Object> map = new HashMap<>();

    public <O> O get(Object o)
    {
        return (O) map.get(o);
    }

    public int size()
    {
        return map.size();
    }

    public boolean isEmpty()
    {
        return map.isEmpty();
    }

    public boolean containsKey(Object o)
    {
        return map.containsKey(o);
    }

    public boolean containsValue(Object o)
    {
        return map.containsValue(o);
    }

    public <O> O put(Object k, O v)
    {
        return (O) map.put(k, v);
    }

    public <O> O remove(Object o)
    {
        return (O) map.remove(o);
    }

    public void putAll(Map<?, ?> m)
    {
        map.putAll(m);
    }

    public void clear()
    {
        map.clear();
    }

    public Set<Object> keySet()
    {
        return map.keySet();
    }

    public <O> Collection<O> values()
    {
        return (Collection<O>) map.values();
    }

    public <O> Set<Map.Entry<Object, O>> entrySet()
    {
        return map.entrySet().stream().map(e -> new HashMap.SimpleEntry<>(e.getKey(), (O) e.getValue())).collect(Collectors.toSet());
    }
}
