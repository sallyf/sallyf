package com.sallyf.sallyf;

import com.sallyf.sallyf.Container.PlainReference;
import com.sallyf.sallyf.Controller.ControllerInterface;
import com.sallyf.sallyf.Router.Router;
import com.sallyf.sallyf.Server.Configuration;
import com.sallyf.sallyf.Server.FrameworkServer;
import com.sallyf.sallyf.Server.RuntimeBagContext;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

class ServerConfiguration extends Configuration
{
    private int port = -1;

    public ServerConfiguration()
    {
        Random r = new Random();

        int low = 2000;
        int high = 9999;

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
        return app.getContainer().get(FrameworkServer.class).getRootURL(RuntimeBagContext.get());
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

        preBoot();

        app.getContainer()
                .getServiceDefinition(FrameworkServer.class)
                .setConfigurationReference(new PlainReference<>(new ServerConfiguration()));

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

    public CookieJar getCookieJar()
    {
        return new CookieJar()
        {
            private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies)
            {
                cookieStore.put(url.host(), cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url)
            {
                List<Cookie> cookies = cookieStore.get(url.host());
                return cookies != null ? cookies : new ArrayList<>();
            }
        };
    }
}
