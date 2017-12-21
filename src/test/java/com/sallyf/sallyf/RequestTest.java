package com.sallyf.sallyf;

import com.sallyf.sallyf.EventDispatcher.EventDispatcher;
import com.sallyf.sallyf.EventDispatcher.EventType;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
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
        super.setUp();

        EventDispatcher eventDispatcher = app.getContainer().get(EventDispatcher.class);

        EventType[] monitoredEvents = {
                KernelEvents.PRE_SEND_RESPONSE,
                KernelEvents.ACTION_FILTER,
                KernelEvents.POST_MATCH_ROUTE,
                KernelEvents.PRE_MATCH_ROUTE,
                KernelEvents.ROUTE_PARAMETERS
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
                KernelEvents.ACTION_FILTER,
                KernelEvents.POST_MATCH_ROUTE,
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
                KernelEvents.ACTION_FILTER,
                KernelEvents.POST_MATCH_ROUTE,
                KernelEvents.PRE_MATCH_ROUTE,
                KernelEvents.ROUTE_PARAMETERS
        };
        assertTrue(dispatchedEvents.containsAll(Arrays.asList(expectedEvents)));
    }
}
