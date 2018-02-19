package com.sallyf.sallyf.Router.JTwigFunctions;

import com.sallyf.sallyf.JTwig.JTwigServiceFunction;
import com.sallyf.sallyf.Router.URLGenerator;
import org.jtwig.functions.FunctionRequest;

import java.util.Collection;
import java.util.HashMap;

public class PathFunction implements JTwigServiceFunction
{
    private URLGenerator urlGenerator;

    public PathFunction(URLGenerator urlGenerator)
    {
        this.urlGenerator = urlGenerator;
    }

    @Override
    public String name()
    {
        return "path";
    }

    @Override
    public Object execute(FunctionRequest request)
    {
        request.minimumNumberOfArguments(1);
        request.maximumNumberOfArguments(2);

        String actionName = (String) request.get(0);

        if (request.getNumberOfArguments() == 1) {
            return urlGenerator.path(actionName);
        }

        HashMap<String, String> parameters = (HashMap<String, String>) request.get(1);

        return urlGenerator.path(actionName, parameters);
    }
}
