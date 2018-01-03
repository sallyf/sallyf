package com.sallyf.sallyf;

import java.lang.reflect.Constructor;

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

    // https://stackoverflow.com/a/9244175/3212099
    public static <T> Constructor<T> getConstructorForArgs(Class<T> klass, Class[] args)
    {
        //Get all the constructors from given class
        Constructor<?>[] constructors = klass.getConstructors();

        for (Constructor<?> constructor : constructors) {
            //Walk through all the constructors, matching parameter amount and parameter types with given types (args)
            Class<?>[] types = constructor.getParameterTypes();
            if (types.length == args.length) {
                boolean argumentsMatch = true;
                for (int i = 0; i < args.length; i++) {
                    //Note that the types in args must be in same order as in the constructor if the checking is done this way
                    if (!types[i].isAssignableFrom(args[i])) {
                        argumentsMatch = false;
                        break;
                    }
                }

                if (argumentsMatch) {
                    //We found a matching constructor, return it
                    return (Constructor<T>) constructor;
                }
            }
        }

        //No matching constructor
        return null;
    }
}
