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

    @Test(expected = AuthenticationException.class)
    public void unknownDataSourceTest() throws AuthenticationException
    {
        AuthenticationManager authenticationManager = new AuthenticationManager(null);

        authenticationManager.getDataSource(InMemoryDataSource.class);
    }

    @Test(expected = AuthenticationException.class)
    public void noDataSourceTest() throws AuthenticationException
    {
        AuthenticationManager authenticationManager = new AuthenticationManager(null);

        authenticationManager.authenticate(null, null, null);
        authenticationManager.authenticate(null, null, null, null);

    }

    @Test(expected = AuthenticationException.class)
    public void ambiguousDataSourceTest() throws AuthenticationException
    {
        AuthenticationManager authenticationManager = new AuthenticationManager(null);

        InMemoryDataSource<User> ds1 = new InMemoryDataSource<>();
        InMemoryDataSource<User> ds2 = new InMemoryDataSource<>();
        authenticationManager.addDataSource(ds1);
        authenticationManager.addDataSource(ds2);

        authenticationManager.authenticate(null, null, null);
        authenticationManager.authenticate(null, null, null, null);
    }

    @Test
    public void dataSourcesTest()
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

    @Test
    public void testAnonymousAccess() throws Exception
    {
        HttpURLConnection http1 = (HttpURLConnection) new URL(getRootURL() + "/secured").openConnection();
        http1.connect();
        assertThat("Response Code", http1.getResponseCode(), is(HttpStatus.FORBIDDEN_403));
    }

    @Test
    public void testAuthenticatedAcces() throws Exception
    {
        HttpURLConnection http1 = (HttpURLConnection) new URL(getRootURL() + "/authenticate").openConnection();
        http1.connect();
        assertThat("Response Code", http1.getResponseCode(), is(HttpStatus.OK_200));

        HttpURLConnection http2 = (HttpURLConnection) new URL(getRootURL() + "/secured").openConnection();
        http2.setRequestProperty("Cookie", http1.getHeaderField("Set-Cookie"));
        http2.connect();
        assertThat("Response Code", http2.getResponseCode(), is(HttpStatus.OK_200));
        assertThat("Content", streamToString(http2), is("Secured"));
    }

}
