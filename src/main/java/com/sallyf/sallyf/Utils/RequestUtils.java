package com.sallyf.sallyf.Utils;

import com.sallyf.sallyf.Exception.FrameworkException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestUtils
{
    public static Map<String, Object> parseQueryPacked(String in, boolean decode)
    {
        return DotNotationUtils.pack(parseQuery(in, decode));
    }

    public static Map<String, Object> parseQuery(Map<String, String[]> in, boolean decode) {
        return null;
    }

    public static Map<String, Object> parseQuery(String in, boolean decode)
    {
        if (decode) {
            try {
                in = URLDecoder.decode(in, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new FrameworkException(e);
            }
        }

        String[] queryComponents = in.split("&");

        HashMap<String, Object> parts = new HashMap<>();

        for (String queryComponent : queryComponents) {
            Map.Entry<String, String> entry = queryComponentToDotNotation(queryComponent);

            if (null != entry) {
                parts.put(entry.getKey(), entry.getValue());
            }
        }

        return parts;
    }

    public static String queryComponentNameToDotNotation(String nameComponents)
    {
        Pattern structurePattern = Pattern.compile("^([\\w]+)(.*)$");
        Pattern nestedPattern = Pattern.compile("\\[(.*?)\\]");

        Matcher structureMatcher = structurePattern.matcher(nameComponents);

        if (structureMatcher.find()) {
            String root = structureMatcher.group(1);
            String nested = structureMatcher.group(2);

            String path = root;

            ArrayList<String> nestedKeys = new ArrayList<>();

            if (!nested.isEmpty()) {
                Matcher nestedMatcher = nestedPattern.matcher(nested);

                while (nestedMatcher.find()) {
                    nestedKeys.add(nestedMatcher.group(1));
                }

                path += "." + String.join(".", nestedKeys);
            }

            return path;
        }

        return null;
    }

    public static Map.Entry<String, String> queryComponentToDotNotation(String queryComponent)
    {
        Pattern globalPattern = Pattern.compile("^(.+)=(.*)$");

        Matcher globalMatcher = globalPattern.matcher(queryComponent);

        if (globalMatcher.find()) {
            String name = globalMatcher.group(1);
            String value = globalMatcher.group(2);

            String nameDotNotation = queryComponentNameToDotNotation(name);

            if (null != nameDotNotation) {
                return new AbstractMap.SimpleEntry<>(nameDotNotation, value);
            }
        }

        return null;
    }
}
