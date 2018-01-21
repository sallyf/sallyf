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
        foo.put("a", a);
        a.put("b", b);
        b.put("c", c);
        c.put("d", "test");
        expected.put("foo", foo);
        expected.put("yo", "lo");
        expected.put("bar", "2");

        String in = URLEncoder.encode("foo[a][b][c][d]=test&yo=lo&bar[]=1&bar[]=2", "UTF-8");

        assertEquals(expected, RequestUtils.parseQueryPacked(in, true));
    }
}
