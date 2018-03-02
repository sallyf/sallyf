package com.sallyf.sallyf;

import com.sallyf.sallyf.Container.ServiceDefinition;
import com.sallyf.sallyf.Exception.UnableToGenerateURLException;
import com.sallyf.sallyf.Router.URLGenerator;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class URLGeneratorTest extends BaseFrameworkTest
{
    @Override
    @Before
    public void setUp() throws Exception
    {
        setUp(TestController.class);
    }

    @Override
    public void preBoot() throws Exception
    {
        super.preBoot();

        app.getContainer().add(new ServiceDefinition<>(CapitalizerResolver.class));
    }

    @Test
    public void testWithoutParameter() throws UnableToGenerateURLException
    {
        URLGenerator urlGenerator = app.getContainer().get(URLGenerator.class);

        String url = urlGenerator.path("test_hello_named");

        assertEquals("/prefixed/hello", url);
    }

    @Test
    public void testWithParameter() throws UnableToGenerateURLException
    {
        URLGenerator urlGenerator = app.getContainer().get(URLGenerator.class);

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("name", "YOLO");

        String url = urlGenerator.path("test_hello5", parameters);

        assertEquals("/prefixed/hello/YOLO", url);
    }

    @Test(expected = UnableToGenerateURLException.class)
    public void testWithParameterException() throws UnableToGenerateURLException
    {
        URLGenerator urlGenerator = app.getContainer().get(URLGenerator.class);

        urlGenerator.path("test_hello5", new HashMap<>());
    }
}
