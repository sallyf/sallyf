package com.sallyf.sallyf.Utils;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RequestTest
{
    @Test
    public void parseTest() throws UnsupportedEncodingException
    {
        Map<String, Object> expected = new HashMap<>();
        Map<String, Object> foo = new HashMap<>();
        Map<String, Object> a = new HashMap<>();
        Map<String, Object> b = new HashMap<>();
        Map<String, Object> c = new HashMap<>();
        Map<String, Object> bar = new HashMap<>();
        Map<String, Object> foo1 = new HashMap<>();
        Map<String, Object> bar1 = new HashMap<>();
        foo.put("a", a);
        a.put("b", b);
        b.put("c", c);
        c.put("d", "test");
        foo1.put("foo", "foovalue");
        bar1.put("bar", "barvalue");
        bar.put("0", foo1);
        bar.put("1", bar1);
        expected.put("foo", foo);
        expected.put("yo", "lo");
        expected.put("bar", bar);

        String in = URLEncoder.encode("foo[a][b][c][d]=test&yo=lo&bar[][foo]=foovalue&bar[][bar]=barvalue", "UTF-8");

        Map<String, Object> actual = RequestUtils.parseQueryPacked(in, true);

        assertEquals(expected, actual);
    }
}
