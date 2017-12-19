package com.sallyf.sallyf;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RequestTest extends BaseFrameworkTest
{
    @Test
    public void testHello() throws IOException
    {
        HttpURLConnection http = (HttpURLConnection) new URL(getRootURL() + "/prefixed/hello").openConnection();
        http.connect();
        assertThat("Response Code", http.getResponseCode(), is(HttpStatus.OK_200));
    }

    @Test
    public void testHelloParameter() throws IOException
    {
        HttpURLConnection http = (HttpURLConnection) new URL(getRootURL() + "/prefixed/hello/YOLO").openConnection();
        http.connect();
        assertThat("Response Code", http.getResponseCode(), is(HttpStatus.OK_200));
        assertThat("Content", streamToString(http), is("hello, YOLO"));
    }
}
