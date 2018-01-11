package com.sallyf.sallyf;

import com.sallyf.sallyf.Container.ServiceDefinition;
import com.sallyf.sallyf.Controller.ControllerInterface;
import com.sallyf.sallyf.Router.Router;
import com.sallyf.sallyf.Server.Configuration;
import com.sallyf.sallyf.Server.FrameworkServer;
import org.junit.After;
import org.junit.Before;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

class ServerConfiguration extends Configuration
{
    private int port = -1;

    public ServerConfiguration()
    {
        Random r = new Random();

        int low = 3000;
        int high = 7000;

        int i = 0;
        while (port == -1 && i < high - low) {
            int tmp = r.nextInt(high - low) + low;

            try {
                ServerSocket serverSocket = new ServerSocket(tmp);
                serverSocket.close();
                port = tmp;
            } catch (IOException ignored) {
            }
            i++;
        }
    }

    @Override
    public int getPort()
    {
        return port;
    }
}

public abstract class BaseFrameworkTest
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

    protected <C extends URLConnection> C followRedirects(C c) throws IOException
    {
        boolean redir;
        int redirects = 0;
        InputStream in;
        do {
            if (c instanceof HttpURLConnection) {
                ((HttpURLConnection) c).setInstanceFollowRedirects(false);
            }
            // We want to open the input stream before getting headers
            // because getHeaderField() et al swallow IOExceptions.
            redir = false;
            if (c instanceof HttpURLConnection) {
                HttpURLConnection http = (HttpURLConnection) c;
                int stat = http.getResponseCode();
                if (stat >= 300 && stat <= 307 && stat != 306 &&
                        stat != HttpURLConnection.HTTP_NOT_MODIFIED) {
                    URL base = http.getURL();
                    String loc = http.getHeaderField("Location");
                    URL target = null;
                    if (loc != null) {
                        target = new URL(base, loc);
                    }
                    http.disconnect();
                    // Redirection should be allowed only for HTTP and HTTPS
                    // and should be limited to 5 redirections at most.
                    if (target == null || !(target.getProtocol().equals("http")
                            || target.getProtocol().equals("https"))
                            || redirects >= 5) {
                        throw new SecurityException("illegal URL redirect");
                    }
                    redir = true;
                    c = (C) target.openConnection();
                    redirects++;
                }
            }
        }
        while (redir);
        return c;
    }

    @Before
    public void setUp() throws Exception
    {
        setUp(null);
    }

    public void preBoot() throws Exception
    {

    }

    public void postBoot() throws Exception
    {

    }

    public void postStart() throws Exception
    {

    }

    public void setUp(Class<? extends ControllerInterface> controllerClass) throws Exception
    {
        app = Kernel.newInstance();

        app.getContainer().add(new ServiceDefinition<>(FrameworkServer.class, new ServerConfiguration()));

        preBoot();

        app.boot();

        Router router = app.getContainer().get(Router.class);

        if (null != controllerClass) {
            router.registerController(controllerClass);
        }

        postBoot();

        app.start();

        postStart();
    }

    @After
    public void tearDown()
    {
        app.stop();
    }
}
