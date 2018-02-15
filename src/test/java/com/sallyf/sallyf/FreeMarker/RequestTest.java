package com.sallyf.sallyf.FreeMarker;

import com.sallyf.sallyf.BaseFrameworkTest;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RequestTest extends BaseFrameworkTest
{
    private OkHttpClient client;

    @Override
    @Before
    public void setUp() throws Exception
    {
        setUp(TestController.class);

        client = new OkHttpClient();
    }

    @Test
    public void testFreeMarkerResponse() throws Exception
    {
        Request request = new Request.Builder()
                .url(getRootURL() + "/freemarker-response")
                .build();

        Response response = client.newCall(request).execute();

        assertThat("Response Code", response.code(), is(200));
        assertThat("Content", response.body().string(), is("Hello world !"));
    }
}
