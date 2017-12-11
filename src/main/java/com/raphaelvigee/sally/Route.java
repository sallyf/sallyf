package com.raphaelvigee.sally;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

enum Method
{
    GET, POST, PUT, PATCH, DELETE
}

class Path
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
}

public class Route
{
    private Method method;

    private Path path;

    private ActionInterface handler;

    public Route(Method method, String pathDeclaration, ActionInterface handler)
    {
        this.method = method;
        this.path = new Path(pathDeclaration);
        this.handler = handler;
    }

    public Method getMethod()
    {
        return method;
    }

    public Path getPath()
    {
        return path;
    }

    public ActionInterface getHandler()
    {
        return handler;
    }

    public RouteParameters getParameters(HTTPSession session)
    {
        Pattern r = Pattern.compile(path.pattern);

        Matcher m = r.matcher(session.getUri());

        RouteParameters parameterValues = new RouteParameters();

        if (m.matches()) {
            path.parameters.forEach((index, name) -> {
                parameterValues.put(name, m.group(index));
            });
        }

        return parameterValues;
    }

    @Override
    public String toString()
    {
        return String.format("%s %s", getMethod(), getPath().declaration);
    }
}
