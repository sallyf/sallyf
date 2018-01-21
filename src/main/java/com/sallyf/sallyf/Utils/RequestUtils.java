package com.sallyf.sallyf.Utils;

import com.sallyf.sallyf.Exception.FrameworkException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestUtils
{
    public static Map<String, Object> parseQuery(String in, boolean decode)
    {
        if (decode) {
            try {
                in = URLDecoder.decode(in, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new FrameworkException(e);
            }
        }

        String[] elements = in.split("&");

        HashMap<String, Object> parts = new HashMap<>();

        Pattern globalPattern = Pattern.compile("^([\\w]+)(.*)=(.*)$");
        Pattern childrenPattern = Pattern.compile("\\[(.*?)\\]");

        for (String element : elements) {
            Matcher globalMatcher = globalPattern.matcher(element);

            if (globalMatcher.find()) {

                String root = globalMatcher.group(1);
                String childrenComponents = globalMatcher.group(2);
                String value = globalMatcher.group(3);

                String path = root;

                ArrayList<String> children = new ArrayList<>();

                if (!childrenComponents.isEmpty()) {
                    Matcher childrenMatcher = childrenPattern.matcher(childrenComponents);

                    while (childrenMatcher.find()) {
                        children.add(childrenMatcher.group(1));
                    }

                    path += "." + String.join(".", children);
                }

                parts.put(path, value);
            }
        }

        return DotNotationUtils.pack(parts);
    }
}
