package com.sallyf.sallyf;

import com.sallyf.sallyf.EventDispatcher.EventDispatcher;
import com.sallyf.sallyf.EventDispatcher.EventType;
import com.sallyf.sallyf.Router.URLGenerator;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class RequestTest extends BaseFrameworkTest
{
    private ArrayList<EventType> dispatchedEvents = new ArrayList<>();

    @Override
    @Before
    public void setUp() throws Exception
    {
        setUp(TestController.class);

        EventDispatcher eventDispatcher = app.getContainer().get(EventDispatcher.class);

        EventType[] monitoredEvents = {
                KernelEvents.PRE_SEND_RESPONSE,
                KernelEvents.POST_MATCH_ROUTE,
                KernelEvents.PRE_MATCH_ROUTE,
                KernelEvents.ROUTE_PARAMETERS,
                KernelEvents.PRE_TRANSFORM_RESPONSE
        };

        eventDispatcher.register(monitoredEvents, (eventType, eventInterface) -> {
            dispatchedEvents.add(eventType);
        });
    }

    @Test
    public void testHello() throws IOException
    {
        HttpURLConnection http = (HttpURLConnection) new URL(getRootURL() + "/prefixed/hello").openConnection();
        http.connect();
        assertThat("Response Code", http.getResponseCode(), is(HttpStatus.OK_200));

        EventType[] expectedEvents = {
                KernelEvents.PRE_SEND_RESPONSE,
                KernelEvents.POST_MATCH_ROUTE,
                KernelEvents.PRE_MATCH_ROUTE,
                KernelEvents.PRE_TRANSFORM_RESPONSE
        };
        assertTrue(dispatchedEvents.containsAll(Arrays.asList(expectedEvents)));
    }

    @Test
    public void test404() throws IOException
    {
        HttpURLConnection http = (HttpURLConnection) new URL(getRootURL() + "/notfound").openConnection();
        http.connect();
        assertThat("Response Code", http.getResponseCode(), is(HttpStatus.NOT_FOUND_404));

        EventType[] expectedEvents = {
                KernelEvents.PRE_MATCH_ROUTE
        };
        assertTrue(dispatchedEvents.containsAll(Arrays.asList(expectedEvents)));
    }

    @Test
    public void testHelloParameter() throws IOException
    {
        HttpURLConnection http = (HttpURLConnection) new URL(getRootURL() + "/prefixed/hello/YOLO").openConnection();
        http.connect();
        assertThat("Response Code", http.getResponseCode(), is(HttpStatus.OK_200));
        assertThat("Content", streamToString(http), is("hello, YOLO"));

        EventType[] expectedEvents = {
                KernelEvents.PRE_SEND_RESPONSE,
                KernelEvents.POST_MATCH_ROUTE,
                KernelEvents.PRE_MATCH_ROUTE,
                KernelEvents.ROUTE_PARAMETERS,
                KernelEvents.PRE_TRANSFORM_RESPONSE
        };
        assertTrue(dispatchedEvents.containsAll(Arrays.asList(expectedEvents)));
    }

    @Test
    public void testTransform() throws IOException
    {
        HttpURLConnection http = (HttpURLConnection) new URL(getRootURL() + "/prefixed/transform/YOLO").openConnection();
        http.connect();
        assertThat("Response Code", http.getResponseCode(), is(HttpStatus.OK_200));
        assertThat("Content", streamToString(http), is("hello, YOLO"));

        EventType[] expectedEvents = {
                KernelEvents.PRE_SEND_RESPONSE,
                KernelEvents.POST_MATCH_ROUTE,
                KernelEvents.PRE_MATCH_ROUTE,
                KernelEvents.ROUTE_PARAMETERS,
                KernelEvents.PRE_TRANSFORM_RESPONSE
        };
        assertTrue(dispatchedEvents.containsAll(Arrays.asList(expectedEvents)));
    }

    @Test
    public void testRedirect() throws Exception
    {
        String target = app.getContainer().get(URLGenerator.class).url("test_hello_named");

        HttpURLConnection http = (HttpURLConnection) new URL(getRootURL() + "/prefixed/redirect").openConnection();
        http.connect();
        http = followRedirects(http);
        assertThat("Target URL", http.getURL().toString(), is(target));
    }
}
