package com.raphaelvigee.sally.Server;

import com.raphaelvigee.sally.BaseFrameworkTest;
import com.raphaelvigee.sally.Kernel;
import com.raphaelvigee.sally.Router.Router;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Request;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ServerTest extends BaseFrameworkTest
{
    private org.eclipse.jetty.server.Response servletResponse;

    @Override
    public void setUp() throws Exception
    {
        app = Kernel.newInstance();

        FrameworkServer server = app.getContainer().add(FrameworkServer.class);
        server.setHandler(new FrameworkHandler(app.getContainer())
        {
            @Override
            public void handle(String target, Request baseRequest, HttpServletRequest servletRequest, HttpServletResponse r)
            {
                super.handle(target, baseRequest, servletRequest, r);

                servletResponse = (org.eclipse.jetty.server.Response) r;
            }
        });

        Router router = app.getContainer().get(Router.class);

        router.addController(TestController.class);

        app.start();
    }

    @Test
    public void responseTest() throws Exception
    {
        HttpURLConnection http = (HttpURLConnection) new URL(getRootURL() + "/test1").openConnection();
        http.connect();
        assertThat("Response Code", http.getResponseCode(), is(HttpStatus.OK_200));
        assertThat("Content", streamToString(http), is("OK"));

        assertThat("Header", servletResponse.getHeader("test1"), is("hello1"));
    }
}
