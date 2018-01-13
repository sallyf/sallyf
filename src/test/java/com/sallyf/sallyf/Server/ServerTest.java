package com.sallyf.sallyf.Server;

import com.sallyf.sallyf.BaseFrameworkTest;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ServerTest extends BaseFrameworkTest
{
    private OkHttpClient client;

    @Override
    public void setUp() throws Exception
    {
        setUp(TestController.class);

        client = new OkHttpClient();
    }

    @Test
    public void responseTest() throws Exception
    {
        Request request = new Request.Builder()
                .url(getRootURL() + "/test1")
                .build();

        Response response = client.newCall(request).execute();

        assertThat("Response Code", response.code(), is(200));
        assertThat("Content", response.body().string(), is("OK"));

        Headers headers = response.headers();

        assertThat("Header", headers.get("test1"), is("hello1"));
        assertThat("Cookie", headers.get("Set-Cookie"), is("cookie1=hello1"));
    }
}
