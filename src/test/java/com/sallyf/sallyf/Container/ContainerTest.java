package com.sallyf.sallyf.Container;

import com.sallyf.sallyf.Container.Exception.CircularReferenceException;
import com.sallyf.sallyf.Container.Exception.ContainerInstantiatedException;
import com.sallyf.sallyf.Container.Exception.ServiceInstantiationException;
import com.sallyf.sallyf.Exception.FrameworkException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

class MyAlias implements ContainerAwareInterface
{
    public MyAlias()
    {
    }
}

class Service1 implements ContainerAwareInterface
{
    public Service1(Service2 service2)
    {
    }
}

class Service2 implements ContainerAwareInterface
{
    public Service2(Service1 service2)
    {
    }
}

class CallService implements ContainerAwareInterface
{
    public CallService()
    {
    }

    Container container;

    public Container getContainer()
    {
        return container;
    }

    public void setContainer(Container container)
    {
        this.container = container;
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

    @Test(expected = ContainerInstantiatedException.class)
    public void testDoubleInstantiation() throws FrameworkException
    {
        Container container = new Container();

        container.instantiateServices();
        container.instantiateServices();
    }

    @Test(expected = ContainerInstantiatedException.class)
    public void testAddAfterInstantiation() throws FrameworkException
    {
        Container container = new Container();

        container.instantiateServices();

        container.add(new ServiceDefinition<>(ExampleServiceExtend.class));
    }

    @Test(expected = CircularReferenceException.class)
    public void testCircularReference() throws FrameworkException
    {
        Container container = new Container();

        container.add(new ServiceDefinition<>(Service1.class));
        container.add(new ServiceDefinition<>(Service2.class));

        container.instantiateServices();
    }

    @Test
    public void testCall() throws FrameworkException
    {
        Container container = new Container();

        ServiceDefinition<CallService> d = new ServiceDefinition<>(CallService.class);
        d.callDefinitions.add(new CallDefinition("setContainer", new ContainerReference()));

        container.add(d);

        container.instantiateServices();

        assertEquals(container, container.get(CallService.class).getContainer());
    }

}
