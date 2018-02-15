package com.sallyf.sallyf.Container;

import com.sallyf.sallyf.Container.Exception.*;
import com.sallyf.sallyf.Exception.FrameworkException;
import org.junit.Test;

import static org.junit.Assert.*;

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

        container.add(new ServiceDefinition<>(Service1Interface.class, Service1.class));
        container.add(new ServiceDefinition<>(Service2.class));

        container.instantiate();
    }

    @Test(expected = ReferenceResolutionException.class)
    public void testMissingReference() throws FrameworkException
    {
        Container container = new Container();

        container.add(new ServiceDefinition<>(Service1.class));

        container.instantiate();
    }

    @Test
    public void testCall() throws FrameworkException
    {
        Container container = new Container();

        container
                .add(new ServiceDefinition<>(ServiceWithCall.class))
                .addMethodCallDefinitions(new MethodCallDefinition("setContainer", new ContainerReference()));

        container.instantiate();

        assertEquals(container, container.get(ServiceWithCall.class).getContainer());
    }

    @Test(expected = ServiceInstantiationException.class)
    public void testInvalidCall() throws FrameworkException
    {
        Container container = new Container();

        container
                .add(new ServiceDefinition<>(ServiceWithCall.class))
                .addMethodCallDefinitions(new MethodCallDefinition("setContainer", new ServiceReference<>(Service1.class)));

        container.instantiate();
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

    @Test(expected = AmbiguousServiceException.class)
    public void testFindAmbiguous()
    {
        Container container = new Container();

        container.add(new ServiceDefinition<>(Service1Ambiguous.class));
        container.add(new ServiceDefinition<>(Service2Ambiguous.class));

        container.instantiate();

        container.find("Ambiguous");
    }

    @Test
    public void testFind()
    {
        Container container = new Container();

        container.add(new ServiceDefinition<>(Service1Ambiguous.class));
        container.add(new ServiceDefinition<>(Service2Ambiguous.class));

        container.instantiate();

        ServiceInterface service = container.find("Service2Ambiguous");

        assertEquals(container.get(Service2Ambiguous.class), service);
    }
}
