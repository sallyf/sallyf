package com.sallyf.sallyf.Container;

import com.sallyf.sallyf.Exception.FrameworkException;
import com.sallyf.sallyf.Exception.ServiceInstantiationException;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

class MyAlias extends ContainerAware
{
    public MyAlias(Container container)
    {
        super(container);
    }
}

public class ContainerTest
{
    @Test
    public void testExtends() throws FrameworkException
    {
        Class c = ExampleServiceExtend.class;

        Container container = new Container();
        container.add(new ServiceDefinition<>(c));

        container.instantiateServices();

        assertNotNull(container.get(c));
    }

    @Test
    public void testImplements() throws FrameworkException
    {
        Class c = ExampleServiceImplements.class;

        Container container = new Container();
        container.add(new ServiceDefinition<>(c));

        container.instantiateServices();

        assertNotNull(container.get(c));
    }

    @Test
    public void testAliasExtends() throws FrameworkException
    {
        Class c = MyAlias.class;

        Container container = new Container();
        container.add(new ServiceDefinition<>(c, ExampleServiceExtend.class));

        container.instantiateServices();

        assertNotNull(container.get(c));
    }

    @Test
    public void testAliasImplements() throws FrameworkException
    {
        Class c = MyAlias.class;

        Container container = new Container();
        container.add(new ServiceDefinition<>(c, ExampleServiceImplements.class));

        container.instantiateServices();

        assertNotNull(container.get(c));
    }

    @Test(expected = ServiceInstantiationException.class)
    public void testInvalid() throws FrameworkException
    {
        Container container = new Container();
        container.add(new ServiceDefinition<>(ExampleServiceInvalid.class));

        container.instantiateServices();
    }
}
