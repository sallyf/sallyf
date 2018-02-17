package com.sallyf.sallyf.JTwig;

import java.util.HashMap;
import java.util.Map;

public class JTwigResponse
{
    private String template;

    private Map<String, Object> data;

    public JTwigResponse(String template)
    {
        this(template, new HashMap<>());
    }

    public JTwigResponse(String template, Map<String, Object> data)
    {
        this.template = template;
        this.data = data;
    }

    public String getTemplate()
    {
        return template;
    }

    public Map<String, Object> getData()
    {
        return data;
    }

}
