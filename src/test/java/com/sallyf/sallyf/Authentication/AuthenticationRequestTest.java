package com.sallyf.sallyf.Authentication;

import com.sallyf.sallyf.AccessDecisionManager.AccessDecisionManager;
import com.sallyf.sallyf.Authentication.DataSource.InMemoryDataSource;
import com.sallyf.sallyf.BaseFrameworkTest;
import com.sallyf.sallyf.Container.Exception.ServiceInstantiationException;
import com.sallyf.sallyf.Container.PlainReference;
import com.sallyf.sallyf.Container.ServiceDefinition;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;

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
    private OkHttpClient client;

    @Override
    public void preBoot() throws ServiceInstantiationException
    {
        app.getContainer()
                .getServiceDefinition(AuthenticationManager.class)
                .setConfigurationReference(new PlainReference<>(new AuthenticationConfiguration()));

        app.getContainer().add(new ServiceDefinition<>(NameVoter.class)).addTag(AccessDecisionManager.TAG_VOTER);
    }

    @Override
    public void setUp() throws Exception
    {
        setUp(TestController.class);

        client = new OkHttpClient.Builder()
                .cookieJar(getCookieJar())
                .build();
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
        Request request1 = new Request.Builder()
                .url(getRootURL() + "/authenticate")
                .build();

        Response response1 = client.newCall(request1).execute();

        assertThat("Response Code", response1.code(), is(200));
        assertThat("Content", response1.body().string(), is("adminadmin"));

        Request request2 = new Request.Builder()
                .url(getRootURL() + "/user")
                .build();

        Response response2 = client.newCall(request2).execute();

        assertThat("Response Code", response2.code(), is(200));
        assertThat("Content", response2.body().string(), is("admin"));
    }

    @Test
    public void testAnonymousAccess() throws Exception
    {
        Request request = new Request.Builder()
                .url(getRootURL() + "/secured")
                .build();

        Response response = client.newCall(request).execute();

        assertThat("Response Code", response.code(), is(403));
    }

    @Test
    public void testAuthenticatedAccess() throws Exception
    {
        Request request1 = new Request.Builder()
                .url(getRootURL() + "/authenticate")
                .build();

        Response response1 = client.newCall(request1).execute();

        assertThat("Response Code", response1.code(), is(200));

        Request request2 = new Request.Builder()
                .url(getRootURL() + "/secured")
                .build();

        Response response2 = client.newCall(request2).execute();

        assertThat("Response Code", response2.code(), is(200));
        assertThat("Content", response2.body().string(), is("Secured"));
    }

    @Test
    public void testParameterVoterSuccess() throws Exception
    {
        Request request = new Request.Builder()
                .url(getRootURL() + "/secured/admin")
                .build();

        Response response = client.newCall(request).execute();

        assertThat("Response Code", response.code(), is(200));
    }

    @Test
    public void testParameterVoterFailure() throws Exception
    {
        Request request = new Request.Builder()
                .url(getRootURL() + "/secured/yolo")
                .build();

        Response response = client.newCall(request).execute();

        assertThat("Response Code", response.code(), is(403));
    }

    @Test
    public void testAuthenticatedParameterVoter() throws Exception
    {
        Request request1 = new Request.Builder()
                .url(getRootURL() + "/authenticate")
                .build();

        Response response1 = client.newCall(request1).execute();

        assertThat("Response Code", response1.code(), is(200));

        Request request2 = new Request.Builder()
                .url(getRootURL() + "/secured/authenticated/admin")
                .build();

        Response response2 = client.newCall(request2).execute();

        assertThat("Response Code", response2.code(), is(200));
        assertThat("Content", response2.body().string(), is("Secured name authenticated"));
    }

    @Test
    public void testAuthenticatedParameterVoterFailure() throws Exception
    {
        Request request1 = new Request.Builder()
                .url(getRootURL() + "/authenticate")
                .build();

        Response response1 = client.newCall(request1).execute();

        assertThat("Response Code", response1.code(), is(200));

        Request request2 = new Request.Builder()
                .url(getRootURL() + "/secured/authenticated/YOLO")
                .build();

        Response response2 = client.newCall(request2).execute();

        assertThat("Response Code", response2.code(), is(403));
    }

}
