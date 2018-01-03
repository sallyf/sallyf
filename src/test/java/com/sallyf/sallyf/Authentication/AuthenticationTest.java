package com.sallyf.sallyf.Authentication;

import com.sallyf.sallyf.Authentication.DataSource.InMemoryDataSource;
import com.sallyf.sallyf.BaseFrameworkTest;
import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ServiceDefinition;
import com.sallyf.sallyf.Exception.ServiceInstantiationException;
import com.sallyf.sallyf.Kernel;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AuthenticationTest extends BaseFrameworkTest
{
    @Test(expected = AuthenticationException.class)
    public void unknownDataSourceTest() throws AuthenticationException
    {
        AuthenticationManager authenticationManager = new AuthenticationManager(new Container(), new Configuration());

        authenticationManager.getDataSource(InMemoryDataSource.class);
    }

    @Test(expected = AuthenticationException.class)
    public void noDataSourceTest() throws AuthenticationException
    {
        AuthenticationManager authenticationManager = new AuthenticationManager(new Container(), new Configuration());

        authenticationManager.authenticate(null, null, null);
        authenticationManager.authenticate(null, null, null, null);

    }

    @Test(expected = AuthenticationException.class)
    public void ambiguousDataSourceTest() throws AuthenticationException
    {
        Configuration configuration = new Configuration()
        {
            @Override
            public ArrayList<UserDataSourceInterface> getDataSources()
            {
                ArrayList<UserDataSourceInterface> dataSources = new ArrayList<>();

                InMemoryDataSource<User> ds1 = new InMemoryDataSource<>();
                InMemoryDataSource<User> ds2 = new InMemoryDataSource<>();

                dataSources.add(ds1);
                dataSources.add(ds2);

                return dataSources;
            }
        };

        AuthenticationManager authenticationManager = new AuthenticationManager(new Container(), configuration);

        authenticationManager.authenticate(null, null, null);
        authenticationManager.authenticate(null, null, null, null);
    }
}
