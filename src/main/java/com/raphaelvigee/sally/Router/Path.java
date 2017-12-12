package com.raphaelvigee.sally.Router;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Path
{
    String declaration;

    String pattern;

    HashMap<Integer, String> parameters = new HashMap<>();

    public Path(String declaration)
    {
        this.declaration = declaration;
        computePattern();
    }

    private void computePattern()
    {
        String parameterName = "(\\{[a-zA-Z0-9-_.]*\\})*";
        String parameterValue = "([^/]*)";

        Pattern r = Pattern.compile(parameterName);

        Matcher m = r.matcher(declaration);

        StringBuffer sb = new StringBuffer("^");
        int i = 1;
        while (m.find()) {
            String declaration = m.group(1);
            if (declaration != null) {
                String name = declaration.substring(1, declaration.length() - 1);

                parameters.put(i, name);
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
}
