package com.raphaelvigee.sally.Authentication;

import com.raphaelvigee.sally.Authentication.DataSource.InMemoryDataSource;
import com.raphaelvigee.sally.BaseFrameworkTest;
import com.raphaelvigee.sally.Exception.ServiceInstanciationException;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

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
    public void testAuthenticateThenRetrieve() throws Exception
    {
        HttpURLConnection http1 = (HttpURLConnection) new URL(getRootURL() + "/authenticate").openConnection();
        http1.connect();
        assertThat("Response Code", http1.getResponseCode(), is(HttpStatus.OK_200));

        HttpURLConnection http2 = (HttpURLConnection) new URL(getRootURL() + "/user").openConnection();
        http2.setRequestProperty("Cookie", http1.getHeaderField("Set-Cookie"));
        http2.connect();
        assertThat("Response Code", http2.getResponseCode(), is(HttpStatus.OK_200));
        assertThat("Content", streamToString(http2), is("admin"));
    }
}
