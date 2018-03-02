package com.sallyf.sallyf;

import com.sallyf.sallyf.Container.ServiceDefinition;
import com.sallyf.sallyf.EventDispatcher.EventDispatcher;
import com.sallyf.sallyf.EventDispatcher.EventType;
import com.sallyf.sallyf.Router.RouteParameterResolverInterface;
import com.sallyf.sallyf.Router.URLGenerator;
import com.sallyf.sallyf.Server.RuntimeBag;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class RequestTest extends BaseFrameworkTest
{
    private ArrayList<EventType> dispatchedEvents = new ArrayList<>();

    private OkHttpClient client;

    @Override
    @Before
    public void setUp() throws Exception
    {
        setUp(TestController.class);

        client = new OkHttpClient();
    }

    @Override
    public void preBoot() throws Exception
    {
        super.preBoot();

        app.getContainer().add(new ServiceDefinition<>(CapitalizerResolver.class));
    }

    @Override
    public void postBoot() throws Exception
    {
        EventDispatcher eventDispatcher = app.getContainer().get(EventDispatcher.class);

        EventType[] monitoredEvents = {
                KernelEvents.PRE_SEND_RESPONSE,
                KernelEvents.POST_MATCH_ROUTE,
                KernelEvents.REQUEST,
                KernelEvents.ROUTE_PARAMETERS,
                KernelEvents.PRE_TRANSFORM_RESPONSE,
                KernelEvents.START,
                KernelEvents.STARTED,
        };

        eventDispatcher.register(monitoredEvents, (eventType, eventInterface) -> {
            dispatchedEvents.add(eventType);
        });
    }

    @Test
    public void testHello() throws IOException
    {
        Request request = new Request.Builder()
                .url(getRootURL() + "/prefixed/hello")
                .build();

        Response response = client.newCall(request).execute();

        assertThat("Response Code", response.code(), is(200));

        EventType[] expectedEvents = {
                KernelEvents.PRE_SEND_RESPONSE,
                KernelEvents.POST_MATCH_ROUTE,
                KernelEvents.REQUEST,
                KernelEvents.PRE_TRANSFORM_RESPONSE,
                KernelEvents.START,
                KernelEvents.STARTED,
        };
        assertTrue(dispatchedEvents.containsAll(Arrays.asList(expectedEvents)));
    }

    @Test
    public void test404() throws IOException
    {
        Request request = new Request.Builder()
                .url(getRootURL() + "/notfound")
                .build();

        Response response = client.newCall(request).execute();

        assertThat("Response Code", response.code(), is(404));

        EventType[] expectedEvents = {
                KernelEvents.REQUEST,
                KernelEvents.START,
                KernelEvents.STARTED,
        };
        assertTrue(dispatchedEvents.containsAll(Arrays.asList(expectedEvents)));
    }

    @Test
    public void testHelloParameter() throws IOException
    {
        Request request = new Request.Builder()
                .url(getRootURL() + "/prefixed/hello/YOLO")
                .build();

        Response response = client.newCall(request).execute();

        assertThat("Response Code", response.code(), is(200));
        assertThat("Content", response.body().string(), is("hello, YOLO fallback"));

        EventType[] expectedEvents = {
                KernelEvents.PRE_SEND_RESPONSE,
                KernelEvents.POST_MATCH_ROUTE,
                KernelEvents.REQUEST,
                KernelEvents.ROUTE_PARAMETERS,
                KernelEvents.PRE_TRANSFORM_RESPONSE,
                KernelEvents.START,
                KernelEvents.STARTED,
        };
        assertTrue(dispatchedEvents.containsAll(Arrays.asList(expectedEvents)));
    }

    @Test
    public void testSlashParameter() throws IOException
    {
        Request request = new Request.Builder()
                .url(getRootURL() + "/prefixed/hello/slashed/i/am/slashed")
                .build();

        Response response = client.newCall(request).execute();

        assertThat("Response Code", response.code(), is(200));
        assertThat("Content", response.body().string(), is("i/am/slashed"));

        EventType[] expectedEvents = {
                KernelEvents.PRE_SEND_RESPONSE,
                KernelEvents.POST_MATCH_ROUTE,
                KernelEvents.REQUEST,
                KernelEvents.ROUTE_PARAMETERS,
                KernelEvents.PRE_TRANSFORM_RESPONSE,
                KernelEvents.START,
                KernelEvents.STARTED,
        };
        assertTrue(dispatchedEvents.containsAll(Arrays.asList(expectedEvents)));
    }

    @Test
    public void testTransform() throws IOException
    {
        Request request = new Request.Builder()
                .url(getRootURL() + "/prefixed/resolve/YOLO")
                .build();

        Response response = client.newCall(request).execute();

        assertThat("Response Code", response.code(), is(200));
        assertThat("Content", response.body().string(), is("hello, YOLO"));

        EventType[] expectedEvents = {
                KernelEvents.PRE_SEND_RESPONSE,
                KernelEvents.POST_MATCH_ROUTE,
                KernelEvents.REQUEST,
                KernelEvents.ROUTE_PARAMETERS,
                KernelEvents.PRE_TRANSFORM_RESPONSE,
                KernelEvents.START,
                KernelEvents.STARTED,
        };
        assertTrue(dispatchedEvents.containsAll(Arrays.asList(expectedEvents)));
    }

    @Test
    public void testRedirect() throws Exception
    {
        String target = app.getContainer().get(URLGenerator.class).url("test_redirect_target");

        AtomicReference<String> lastUrl = new AtomicReference<>("");

        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(chain -> {
                    String next = chain.request().url().toString();

                    lastUrl.set(next);

                    return chain.proceed(chain.request());
                })
                .build();

        Request request = new Request.Builder()
                .url(getRootURL() + "/prefixed/redirect")
                .build();

        client.newCall(request).execute();

        assertThat("Target URL", lastUrl.get(), is(target));
    }

    @Test
    public void testInvalidResponse() throws Exception
    {
        Request request = new Request.Builder()
                .url(getRootURL() + "/prefixed/invalidresponse")
                .build();

        Response response = client.newCall(request).execute();

        assertThat("Response Code", response.code(), is(500));
    }

    @Test
    public void testParameterResolver() throws Exception
    {
        Request request = new Request.Builder()
                .url(getRootURL() + "/prefixed/resolver/hello")
                .build();

        Response response = client.newCall(request).execute();

        assertThat("Response Code", response.code(), is(200));
        assertThat("Content", response.body().string(), is("HELLO"));
    }
}
