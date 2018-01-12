package com.sallyf.sallyf.Exception;

import java.util.HashMap;

public class UnableToGenerateURLException extends FrameworkException
{
    public UnableToGenerateURLException(String action, HashMap<String, String> parameters)
    {
        super("Unable to generate URL: " + action + parameters);

    }
}
