package com.sallyf.sallyf.FlashManager;

import com.sallyf.sallyf.BaseFrameworkTest;
import com.sallyf.sallyf.Server.Status;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FlashManagerTest extends BaseFrameworkTest
{
    private OkHttpClient client;

    @Override
    public void setUp() throws Exception
    {
        setUp(TestController.class);

        client = new OkHttpClient.Builder()
                .cookieJar(getCookieJar())
                .build();
    }

    @Test
    public void testEmpty() throws Exception
    {
        Request request = new Request.Builder()
                .url(getRootURL() + "/read-flashes")
                .build();

        Response response = client.newCall(request).execute();

        assertEquals(Status.OK.getRequestStatus(), response.code());

        assertEquals("", response.body().string());
    }

    @Test
    public void testAddRead() throws Exception
    {
        Request request1 = new Request.Builder()
                .url(getRootURL() + "/add-flashes")
                .build();

        Response response1 = client.newCall(request1).execute();

        assertEquals(Status.OK.getRequestStatus(), response1.code());

        Request request2 = new Request.Builder()
                .url(getRootURL() + "/read-flashes")
                .build();

        Response response2 = client.newCall(request2).execute();

        assertEquals(Status.OK.getRequestStatus(), response2.code());

        assertEquals("Flash 1 Flash 2 Flash 3", response2.body().string());
    }

    @Test
    public void testAddForwardRead() throws Exception
    {
        Request request1 = new Request.Builder()
                .url(getRootURL() + "/add-flashes")
                .build();

        Response response1 = client.newCall(request1).execute();

        assertEquals(Status.OK.getRequestStatus(), response1.code());

        Request request2 = new Request.Builder()
                .url(getRootURL() + "/forward")
                .build();

        Response response2 = client.newCall(request2).execute();

        assertEquals(Status.OK.getRequestStatus(), response2.code());

        Request request3 = new Request.Builder()
                .url(getRootURL() + "/read-flashes")
                .build();

        Response response3 = client.newCall(request3).execute();

        assertEquals(Status.OK.getRequestStatus(), response3.code());

        assertEquals("Flash 1 Flash 2 Flash 3", response3.body().string());
    }

    @Test
    public void testAddRedirectRead() throws Exception
    {
        Request request1 = new Request.Builder()
                .url(getRootURL() + "/add-flashes")
                .build();

        Response response1 = client.newCall(request1).execute();

        assertEquals(Status.OK.getRequestStatus(), response1.code());

        Request request2 = new Request.Builder()
                .url(getRootURL() + "/redirect")
                .build();

        client.newCall(request2).execute();

        Request request3 = new Request.Builder()
                .url(getRootURL() + "/read-flashes")
                .build();

        Response response3 = client.newCall(request3).execute();

        assertEquals(Status.OK.getRequestStatus(), response3.code());

        assertEquals("Flash 1 Flash 2 Flash 3", response3.body().string());
    }
}
