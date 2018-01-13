package com.sallyf.sallyf.ExpressionLanguage;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ServiceDefinition;
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
    public void setUp() throws Exception
    {
        container = new Container();

        container.add(new ServiceDefinition<>(TestService.class));
        container.add(new ServiceDefinition<>(ExpressionLanguage.class));

        container.instantiate();

        expressionLanguage = container.get(ExpressionLanguage.class);
    }

    @Test
    public void evalSimpleTest() throws Exception
    {
        Integer result = expressionLanguage.<Integer>evaluate("1 + 1");

        assertEquals((Integer) 2, result);
    }

    @Test
    public void evalServiceTest() throws Exception
    {
        TestService result = expressionLanguage.evaluate("service('com.sallyf.sallyf.ExpressionLanguage.TestService')");

        assertEquals(container.get(TestService.class), result);
    }

    @Test
    public void evalBooleanTest() throws Exception
    {
        Boolean result = expressionLanguage.evaluate("service('com.sallyf.sallyf.ExpressionLanguage.TestService').returnTrue()");

        assertTrue(result);
    }

    @Test
    public void evalHashMapTest() throws Exception
    {
        HashMap<String, ArrayList<Status>> result = expressionLanguage.evaluate("service('com.sallyf.sallyf.ExpressionLanguage.TestService').returnHashMapOfArrayListOfStatus()");

        assertEquals(Status.OK, result.get("statuses").get(0));
    }
}
