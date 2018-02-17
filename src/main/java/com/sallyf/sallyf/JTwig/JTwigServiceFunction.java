package com.sallyf.sallyf.JTwig;

import com.sallyf.sallyf.Container.ServiceInterface;
import org.jtwig.functions.JtwigFunction;

import java.util.ArrayList;
import java.util.Collection;

public interface JTwigServiceFunction extends JtwigFunction, ServiceInterface
{
    @Override
    default Collection<String> aliases()
    {
        return new ArrayList<>();
    }
}
