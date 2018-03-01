package com.sallyf.sallyf.Utils;

public class HTMLUtils
{
    public static String escapeHtmlAttribute(String attribute)
    {
        return attribute
                .replaceAll("\"", "&quot;")
                .replaceAll("'", "&#x27;");
    }
}
