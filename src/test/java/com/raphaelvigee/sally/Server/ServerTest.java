package com.raphaelvigee.sally.Server;

import com.raphaelvigee.sally.BaseFrameworkTest;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ServerTest extends BaseFrameworkTest
{
    private org.eclipse.jetty.server.Response servletResponse;

    @Override
    public void setUp() throws Exception
    {
        setUp(TestController.class);
    }

    @Test
    public void responseTest() throws Exception
    {
        HttpURLConnection http = (HttpURLConnection) new URL(getRootURL() + "/test1").openConnection();
        http.connect();
        assertThat("Response Code", http.getResponseCode(), is(HttpStatus.OK_200));
        assertThat("Content", streamToString(http), is("OK"));

        Map<String, List<String>> headerFields = http.getHeaderFields();

        assertThat("Header", headerFields.get("test1").get(0), is("hello1"));
        assertThat("Cookie", headerFields.get("Set-Cookie").get(0), is("cookie1=hello1"));
    }
}
