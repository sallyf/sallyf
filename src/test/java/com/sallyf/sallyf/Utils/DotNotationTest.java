package com.sallyf.sallyf.Utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class DotNotationTest
{
    private HashMap<String, Object> testData()
    {
        ArrayList<String> numbersList = new ArrayList<>();
        numbersList.add("one");
        numbersList.add("two");

        HashSet<String> numbersSet = new HashSet<>();
        numbersSet.add("one");
        numbersSet.add("two");

        HashMap<String, Object> hashmap = new HashMap<>();
        hashmap.put("numbers", numbersList);

        HashMap<String, Object> toFlatten = new HashMap<>();
        toFlatten.put("value", "valueOne");
        toFlatten.put("numbersList", numbersList);
        toFlatten.put("numbersSet", numbersSet);
        toFlatten.put("hashmap", hashmap);


        return toFlatten;
    }

    @Test
    public void accessTest()
    {
        HashMap<String, Object> toFlatten = testData();

        assertEquals("valueOne", DotNotationUtils.access(toFlatten, "value"));
        assertEquals("two", DotNotationUtils.access(toFlatten, "numbersList.1"));
        assertEquals("one", DotNotationUtils.access(toFlatten, "hashmap.numbers.0"));
    }

    @Test
    public void flattenTest()
    {
        HashMap<String, Object> toFlatten = testData();

        Set<String> flattenLeafsOnly = DotNotationUtils.flattenKeys(toFlatten, true);

        List<String> flattenLeafsOnlyList = new ArrayList<>(flattenLeafsOnly);

        List<String> expectedLeafsOnly = new ArrayList<String>()
        {
            {
                add("value");
                add("numbersList.0");
                add("numbersList.1");
                add("numbersSet.0");
                add("numbersSet.1");
                add("hashmap.numbers.0");
                add("hashmap.numbers.1");
            }
        };

        Collections.sort(expectedLeafsOnly);
        Collections.sort(flattenLeafsOnlyList);

        assertEquals(expectedLeafsOnly, flattenLeafsOnlyList);

        Set<String> flattenFull = DotNotationUtils.flattenKeys(toFlatten, false);

        List<String> flattenFullList = new ArrayList<>(flattenFull);

        List<String> expectedFull = new ArrayList<String>()
        {
            {
                add("value");
                add("numbersList");
                add("numbersList.0");
                add("numbersList.1");
                add("numbersSet");
                add("numbersSet.0");
                add("numbersSet.1");
                add("hashmap");
                add("hashmap.numbers");
                add("hashmap.numbers.0");
                add("hashmap.numbers.1");
            }
        };
        Collections.sort(expectedFull);
        Collections.sort(flattenFullList);

        assertEquals(expectedFull, flattenFullList);
    }

    @Test
    public void packTest()
    {
        Map<String, Object> flatMap = new HashMap<>();
        flatMap.put("foo.bar.test", "yolo");
        flatMap.put("foo", new HashMap<>());
        flatMap.put("yolo", "yo");

        HashMap<String, Object> expected = new HashMap<>();
        HashMap<String, HashMap> foo = new HashMap<>();
        HashMap<String, String> bar = new HashMap<>();
        bar.put("test", "yolo");
        foo.put("bar", bar);
        expected.put("foo", foo);
        expected.put("yolo", "yo");

        Assert.assertEquals(expected, DotNotationUtils.pack(flatMap));
    }

    @Test
    public void flattenPackTest()
    {
        Map<String, Object> flatMap = new HashMap<>();
        flatMap.put("foo.bar.test", "yolo");
        flatMap.put("foo", new HashMap<>());
        flatMap.put("yolo", "yo");

        Map<String, Object> packedMap = DotNotationUtils.pack(flatMap);
        Map<String, Object> reFlattenMap = DotNotationUtils.flatten(packedMap);

        Map<String, Object> expected = new HashMap<>();
        expected.put("foo.bar.test", "yolo");
        expected.put("yolo", "yo");

        Assert.assertEquals(expected, reFlattenMap);
    }
}
