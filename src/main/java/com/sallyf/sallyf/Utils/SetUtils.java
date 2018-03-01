package com.sallyf.sallyf.Utils;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SetUtils
{
    public static <M extends Set<E>, E> M createSet(Class<M> setClass, E... entries)
    {
        return Stream.of(entries).collect(Collectors.toCollection(() -> ClassUtils.newInstance(setClass)));
    }

    @SafeVarargs
    public static <E> HashSet<E> createHashSet(E... entries)
    {
        return createSet(LinkedHashSet.class, entries);
    }

    @SafeVarargs
    public static <E> LinkedHashSet<E> createLinkedHashSet(E... entries)
    {
        return createSet(LinkedHashSet.class, entries);
    }
}
