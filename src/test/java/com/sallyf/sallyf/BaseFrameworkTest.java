package com.sallyf.sallyf;

import com.sallyf.sallyf.Router.Router;
import com.sallyf.sallyf.Server.FrameworkServer;
import org.junit.After;
import org.junit.Before;

import java.io.*;
import java.net.HttpURLConnection;

abstract class BaseFrameworkTest
{
    protected Kernel app;

    protected String getRootURL()
    {
        return app.getContainer().get(FrameworkServer.class).getRootURL();
    }

    protected String streamToString(HttpURLConnection connection)
    {
        String result = null;
        StringBuffer sb = new StringBuffer();
        InputStream is = null;

        try {
            is = new BufferedInputStream(connection.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    @Before
    public void setUp() throws Exception
    {
        app = Kernel.newInstance();

        Router router = app.getContainer().get(Router.class);

        router.addController(TestController.class);

        app.start();
    }

    @After
    public void tearDown()
    {
        app.stop();
    }
}
