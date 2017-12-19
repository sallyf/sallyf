package com.raphaelvigee.sally;

import com.raphaelvigee.sally.Exception.UnableToGenerateURLException;
import com.raphaelvigee.sally.Router.URLGenerator;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class URLGeneratorTest extends BaseFrameworkTest
{
    @Test
    public void testWithoutParameter() throws UnableToGenerateURLException
    {
        URLGenerator urlGenerator = app.getContainer().get(URLGenerator.class);

        String url = urlGenerator.path("TestController.hello4");

        assertEquals("/prefixed/hello", url);
    }

    @Test
    public void testWithParameter() throws UnableToGenerateURLException
    {
        URLGenerator urlGenerator = app.getContainer().get(URLGenerator.class);

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("name", "YOLO");

        String url = urlGenerator.path("TestController.hello5", parameters);

        assertEquals("/prefixed/hello/YOLO", url);
    }

    @Test(expected = UnableToGenerateURLException.class)
    public void testWithParameterException() throws UnableToGenerateURLException
    {
        URLGenerator urlGenerator = app.getContainer().get(URLGenerator.class);

        urlGenerator.path("TestController.hello5", new HashMap<>());
    }
}
