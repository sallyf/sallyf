package com.sallyf.sallyf.Container;

import com.sallyf.sallyf.Container.Exception.CircularReferenceException;
import com.sallyf.sallyf.Container.Exception.ContainerInstantiatedException;
import com.sallyf.sallyf.Container.Exception.ServiceInstantiationException;
import com.sallyf.sallyf.Exception.FrameworkException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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

class ServiceWithCall implements ContainerAwareInterface
{
    public ServiceWithCall()
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

        container.instantiate();

        assertNotNull(container.get(c));
    }

    @Test
    public void testImplements() throws FrameworkException
    {
        Class c = ExampleServiceImplements.class;

        Container container = new Container();
        container.add(new ServiceDefinition<>(c));

        container.instantiate();

        assertNotNull(container.get(c));
    }

    @Test
    public void testAliasExtends() throws FrameworkException
    {
        Class c = MyAlias.class;

        Container container = new Container();
        container.add(new ServiceDefinition<>(c, ExampleServiceExtend.class));

        container.instantiate();

        assertNotNull(container.get(c));
    }

    @Test
    public void testAliasImplements() throws FrameworkException
    {
        Class c = MyAlias.class;

        Container container = new Container();
        container.add(new ServiceDefinition<>(c, ExampleServiceImplements.class));

        container.instantiate();

        assertNotNull(container.get(c));
    }

    @Test(expected = ServiceInstantiationException.class)
    public void testInvalid() throws FrameworkException
    {
        Container container = new Container();
        container.add(new ServiceDefinition<>(ExampleServiceInvalid.class));

        container.instantiate();
    }

    @Test(expected = ContainerInstantiatedException.class)
    public void testDoubleInstantiation() throws FrameworkException
    {
        Container container = new Container();

        container.instantiate();
        container.instantiate();
    }

    @Test(expected = ContainerInstantiatedException.class)
    public void testAddAfterInstantiation() throws FrameworkException
    {
        Container container = new Container();

        container.instantiate();

        container.add(new ServiceDefinition<>(ExampleServiceExtend.class));
    }

    @Test(expected = CircularReferenceException.class)
    public void testCircularReference() throws FrameworkException
    {
        Container container = new Container();

        container.add(new ServiceDefinition<>(Service1.class));
        container.add(new ServiceDefinition<>(Service2.class));

        container.instantiate();
    }

    @Test
    public void testCall() throws FrameworkException
    {
        Container container = new Container();

        container
                .add(new ServiceDefinition<>(ServiceWithCall.class))
                .addCallDefinitions(new CallDefinition("setContainer", new ContainerReference()));

        container.instantiate();

        assertEquals(container, container.get(ServiceWithCall.class).getContainer());
    }

    @Test(expected = ServiceInstantiationException.class)
    public void testInvalidCall() throws FrameworkException
    {
        Container container = new Container();

        container
                .add(new ServiceDefinition<>(ServiceWithCall.class))
                .addCallDefinitions(new CallDefinition("setContainer", new ServiceReference<>(Service1.class)));

        container.instantiate();

        assertEquals(container, container.get(ServiceWithCall.class).getContainer());
    }

    @Test
    public void testWithoutDefaultConfiguration() throws FrameworkException
    {
        Container container = new Container();

        container.add(new ServiceDefinition<>(ServiceWithoutDefaultConfiguration.class));

        container.instantiate();

        ServiceWithoutDefaultConfiguration service = container.get(ServiceWithoutDefaultConfiguration.class);

        assertNull(service.configuration);
    }

    @Test
    public void testDefaultConfigurationAutoWiring() throws FrameworkException
    {
        Container container = new Container();

        container.add(new ServiceDefinition<>(ServiceWithConfiguration.class));

        container.instantiate();

        ServiceWithConfiguration service = container.get(ServiceWithConfiguration.class);

        assertEquals(1, service.configuration.getNumber());
    }

    @Test
    public void testConfigurationManualDeclaration() throws FrameworkException
    {
        Container container = new Container();

        container.add(new ServiceDefinition<>(ServiceWithConfiguration.class, new ServiceWithConfiguration.CustomServiceConfiguration()));

        container.instantiate();

        ServiceWithConfiguration service = container.get(ServiceWithConfiguration.class);

        assertEquals(2, service.configuration.getNumber());
    }

    @Test
    public void testConfigurationManualDeclarationInContainer() throws FrameworkException
    {
        Container container = new Container();

        container.add(new ServiceDefinition<>(ServiceWithConfiguration.class));

        container.setConfiguration(ServiceWithConfiguration.class, new ServiceWithConfiguration.CustomServiceConfiguration());

        container.instantiate();

        ServiceWithConfiguration service = container.get(ServiceWithConfiguration.class);

        assertEquals(2, service.configuration.getNumber());
    }
}
