package com.sallyf.sallyf.Utils;

import com.sallyf.sallyf.Exception.FrameworkException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.function.Consumer;

public class ClassUtils
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
                    if (args[i] == null) {
                        continue;
                    }

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

    public static <C> C newInstance(Class<C> klass, Object... args)
    {
        return newInstance(klass, e -> {throw new FrameworkException(e);}, args);
    }

    public static <C> C newInstance(Class<C> klass, Consumer<Exception> exceptionHandler, Object... args)
    {
        Class[] argClasses = ClassUtils.getClasses(args);

        try {
            Constructor<C> constructor = getConstructorForArgs(klass, argClasses);

            if (null == constructor) {
                throw new NoSuchMethodException("Unable to find constructor for: " + klass + " with args: " + Arrays.toString(argClasses));
            }

            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            exceptionHandler.accept(e);
        }

        return null;
    }
}
