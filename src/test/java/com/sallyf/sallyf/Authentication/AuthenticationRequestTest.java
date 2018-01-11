package com.sallyf.sallyf.Authentication;

import com.sallyf.sallyf.Authentication.DataSource.InMemoryDataSource;
import com.sallyf.sallyf.BaseFrameworkTest;
import com.sallyf.sallyf.Container.Exception.ServiceInstantiationException;
import com.sallyf.sallyf.Container.ServiceDefinition;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

class AuthenticationConfiguration extends Configuration
{
    @Override
    public ArrayList<UserDataSourceInterface> getDataSources()
    {
        ArrayList<UserDataSourceInterface> dataSources = new ArrayList<>();

        InMemoryDataSource<User> memoryDS = new InMemoryDataSource<>();
        memoryDS.addUser(new User("admin", "password"));
        memoryDS.addUser(new User("user1", "password"));
        memoryDS.addUser(new User("user2", "password"));

        dataSources.add(memoryDS);

        return dataSources;
    }
}

public class AuthenticationRequestTest extends BaseFrameworkTest
{
    @Override
    public void preBoot() throws ServiceInstantiationException
    {
        app.getContainer().add(new ServiceDefinition<>(AuthenticationManager.class, new AuthenticationConfiguration()));
        app.getContainer().add(new ServiceDefinition<>(NameVoter.class)).addTag(AuthenticationManager.TAG_VOTER);
    }

    @Override
    public void setUp() throws Exception
    {
        setUp(TestController.class);
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
    public void testAuthenticatedAccess() throws Exception
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

    @Test
    public void testParameterVoterSuccess() throws Exception
    {
        HttpURLConnection http = (HttpURLConnection) new URL(getRootURL() + "/secured/admin").openConnection();
        http.connect();
        assertThat("Response Code", http.getResponseCode(), is(HttpStatus.OK_200));
    }

    @Test
    public void testParameterVoterFailure() throws Exception
    {
        HttpURLConnection http = (HttpURLConnection) new URL(getRootURL() + "/secured/yolo").openConnection();
        http.connect();
        assertThat("Response Code", http.getResponseCode(), is(HttpStatus.FORBIDDEN_403));
    }
}
