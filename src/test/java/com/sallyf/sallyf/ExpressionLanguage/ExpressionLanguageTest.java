package com.sallyf.sallyf.ExpressionLanguage;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ServiceDefinition;
import com.sallyf.sallyf.EventDispatcher.EventDispatcher;
import com.sallyf.sallyf.Exception.FrameworkException;
import com.sallyf.sallyf.Router.Router;
import com.sallyf.sallyf.Server.Status;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ExpressionLanguageTest
{
    private ExpressionLanguage expressionLanguage;

    private Container container;

    @Before
    public void setUp()
    {
        container = new Container();

        container.add(new ServiceDefinition<>(TestService.class));
        container.add(new ServiceDefinition<>(EventDispatcher.class));
        container.add(new ServiceDefinition<>(Router.class));
        container.add(new ServiceDefinition<>(ExpressionLanguage.class));

        container.instantiate();

        expressionLanguage = container.get(ExpressionLanguage.class);
    }

    @Test
    public void evalSimpleTest()
    {
        Integer result = expressionLanguage.evaluate("1 + 1");

        assertEquals((Integer) 2, result);
    }

    @Test
    public void evalServiceTest()
    {
        TestService result = expressionLanguage.evaluate("service('com.sallyf.sallyf.ExpressionLanguage.TestService')");

        assertEquals(container.get(TestService.class), result);
    }

    @Test
    public void evalShortServiceTest()
    {
        TestService result = expressionLanguage.evaluate("service('TestService')");

        assertEquals(container.get(TestService.class), result);
    }

    @Test
    public void evalBooleanTest()
    {
        Boolean result = expressionLanguage.evaluate("service('TestService').returnTrue()");

        assertTrue(result);
    }

    @Test
    public void evalHashMapTest()
    {
        HashMap<String, ArrayList<Status>> result = expressionLanguage.evaluate("service('TestService').returnHashMapOfArrayListOfStatus()");

        assertEquals(Status.OK, result.get("statuses").get(0));
    }

    @Test
    public void evalFullHashMapTest()
    {
        Status result = expressionLanguage.evaluate("service('TestService').returnHashMapOfArrayListOfStatus().get('statuses').get(0)");

        assertEquals(Status.OK, result);
    }

    @Test(expected = FrameworkException.class)
    public void evalNonExistentServiceTest()
    {
        expressionLanguage.evaluate("service('YOLO')");
    }
}
