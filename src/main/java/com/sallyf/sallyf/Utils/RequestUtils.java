package com.sallyf.sallyf.Utils;

import com.sallyf.sallyf.Exception.FrameworkException;
import org.eclipse.jetty.server.Request;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

        String[] queryComponents = in.split("&");

        Map<List<String>, String> queryMap = getQueryMapPath(queryComponents);

        return expandQueryMapPath(queryMap);
    }

    private static Map<List<String>, String> getQueryMapPath(String[] queryComponents)
    {
        Map<List<String>, String> queryMap = new HashMap<>();

        Map<String, Integer> undefinedKeyIndexes = new HashMap<>();

        for (String queryComponent : queryComponents) {
            Pattern keyValuePattern = Pattern.compile("^(.+)=(.*)$");

            Matcher keyValueMatcher = keyValuePattern.matcher(queryComponent);

            if (keyValueMatcher.find()) {
                String key = keyValueMatcher.group(1);
                String value = keyValueMatcher.group(2);

                Pattern structurePattern = Pattern.compile("^([a-zA-Z0-9-_]+)(.*)$");

                Matcher structureMatcher = structurePattern.matcher(key);

                if (structureMatcher.find()) {
                    List<String> path = new ArrayList<>();

                    String root = structureMatcher.group(1);
                    String nested = structureMatcher.group(2);

                    path.add(root);

                    Pattern nestedPattern = Pattern.compile("\\[(.*?)\\]");

                    Matcher nestedMatcher = nestedPattern.matcher(nested);

                    while (nestedMatcher.find()) {
                        String dotPath = String.join(".", path);

                        int undefinedKeyIndex = undefinedKeyIndexes.getOrDefault(dotPath, 0);

                        String nKey = nestedMatcher.group(1);

                        if (nKey.isEmpty()) {
                            nKey = String.valueOf(undefinedKeyIndex++);
                            undefinedKeyIndexes.put(dotPath, undefinedKeyIndex);
                        }

                        path.add(nKey);
                    }

                    queryMap.put(path, value);
                }
            }
        }

        return queryMap;
    }

    private static Map<String, Object> expandQueryMapPath(Map<List<String>, String> queryMap)
    {
        Map<String, Object> out = new HashMap<>();

        for (Map.Entry<List<String>, String> entry : queryMap.entrySet()) {
            List<String> fullPath = entry.getKey();
            String value = entry.getValue();

            Map<String, Object> parentNode;

            for (int level = 0; level < fullPath.size(); level++) {
                boolean isLast = (level == fullPath.size() - 1);

                String key = fullPath.get(level);

                if (level == 0) {
                    parentNode = out;
                } else {
                    List<String> parentPath = fullPath.stream().limit(level).collect(Collectors.toList());

                    parentNode = DotNotationUtils.access(out, String.join(".", parentPath));
                }

                if (isLast) {
                    parentNode.put(key, value);
                } else {
                    if (!parentNode.containsKey(key)) {
                        parentNode.put(key, new HashMap<>());
                    }
                }

            }
        }

        return out;
    }
}
