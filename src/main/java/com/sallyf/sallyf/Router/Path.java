package com.sallyf.sallyf.Router;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Path
{
    private final static String PARAMETER_NAME = "(\\{[a-zA-Z0-9-_.]*\\})*";

    private final static String PARAMETER_VALUE = "([^/]*)";

    private String declaration;

    private String pattern;

    private HashMap<Integer, String> parameters = new HashMap<>();

    private HashMap<String, String> requirements = new HashMap<>();

    public Path(String declaration)
    {
        this.declaration = declaration;
    }

    public void computePattern()
    {
        Pattern r = Pattern.compile(PARAMETER_NAME);

        Matcher m = r.matcher(declaration);

        StringBuffer sb = new StringBuffer("^");
        int i = 1;
        while (m.find()) {
            String declaration = m.group(1);
            if (declaration != null) {
                String name = declaration.substring(1, declaration.length() - 1);

                parameters.put(i, name);

                String parameterValue = requirements.getOrDefault(name, PARAMETER_VALUE);

                m.appendReplacement(sb, Matcher.quoteReplacement(parameterValue));
                i++;
            }
        }
        m.appendTail(sb);
        sb.append("$");

        pattern = sb.toString();
    }

    public String getDeclaration()
    {
        return declaration;
    }

    public String getPattern()
    {
        return pattern;
    }

    public HashMap<Integer, String> getParameters()
    {
        return parameters;
    }

    public HashMap<String, String> getRequirements()
    {
        return requirements;
    }
}
