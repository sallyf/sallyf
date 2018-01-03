package com.sallyf.sallyf;

public class Utils
{
    public static Class[] getClasses(Object[] args)
    {
        Class[] classes = new Class[args.length];
        int i = 0;
        for (Object parameter : args) {
            if (parameter == null) {
                continue;
            }

            Class parameterClass = parameter.getClass();
            classes[i++] = parameterClass;
        }

        return classes;
    }
}
