package com.sallyf.sallyf.Authentication;

import com.sallyf.sallyf.Authentication.DataSource.InMemoryDataSource;
import com.sallyf.sallyf.BaseFrameworkTest;
import com.sallyf.sallyf.Exception.ServiceInstanciationException;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.net.URL;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AuthenticationTest extends BaseFrameworkTest
{
    @Override
    public void initBeforeRoute() throws ServiceInstanciationException
    {
        AuthenticationManager authenticationManager = app.getContainer().add(AuthenticationManager.class);

        InMemoryDataSource<User> memoryDS = new InMemoryDataSource<>();
        memoryDS.addUser(new User("admin", "password"));
        memoryDS.addUser(new User("user1", "password"));
        memoryDS.addUser(new User("user2", "password"));
        authenticationManager.addDataSource(memoryDS);
    }

    @Override
    public void setUp() throws Exception
    {
        setUp(TestController.class);
    }

    @Test
    public void dataSourcesTest() throws AuthenticationException
    {
        AuthenticationManager authenticationManager = app.getContainer().get(AuthenticationManager.class);

        assertThat("Count", authenticationManager.getDataSources().size(), is(1));
    }

    @Test
    public void testAuthenticateThenRetrieve() throws Exception
    {
        HttpURLConnection http1 = (HttpURLConnection) new URL(getRootURL() + "/authenticate").openConnection();
        http1.connect();
        assertThat("Response Code", http1.getResponseCode(), is(HttpStatus.OK_200));
        assertThat("Content", streamToString(http1), is("adminadmin"));

        HttpURLConnection http2 = (HttpURLConnection) new URL(getRootURL() + "/user").openConnection();
        http2.setRequestProperty("Cookie", http1.getHeaderField("Set-Cookie"));
        http2.connect();
        assertThat("Response Code", http2.getResponseCode(), is(HttpStatus.OK_200));
        assertThat("Content", streamToString(http2), is("admin"));
    }
}
