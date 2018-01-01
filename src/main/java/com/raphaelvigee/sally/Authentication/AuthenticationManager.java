package com.raphaelvigee.sally.Authentication;

import com.raphaelvigee.sally.Container.Container;
import com.raphaelvigee.sally.Container.ContainerAware;

import java.util.ArrayList;

public class AuthenticationManager extends ContainerAware
{
    ArrayList<UserDataSourceInterface> dataSources = new ArrayList<>();

    public AuthenticationManager(Container container)
    {
        super(container);
    }

    public UserInterface authenticate(String username, String password) throws AuthenticationException
    {
        return authenticate(username, password, null);
    }

    public UserInterface authenticate(String username, String password, Class<UserDataSourceInterface> dataSourceClass) throws AuthenticationException
    {
        if (dataSources.size() == 0) {
            throw new AuthenticationException("No datasource provided");
        }

        UserDataSourceInterface dataSource;

        if (dataSourceClass == null) {
            if (dataSources.size() == 1) {
                dataSource = dataSources.get(0);
            } else {
                throw new AuthenticationException("Ambiguous datasource");
            }
        } else {
            dataSource = getDataSource(dataSourceClass);
        }

        return dataSource.getUser(username, password);
    }

    public void addDataSource(UserDataSourceInterface ds)
    {
        dataSources.add(ds);
    }

    public ArrayList<UserDataSourceInterface> getDataSources()
    {
        return dataSources;
    }

    public UserDataSourceInterface getDataSource(Class<UserDataSourceInterface> dataSourceClass) throws AuthenticationException
    {
        for (UserDataSourceInterface dataSource : dataSources) {
            if (dataSource.getClass().equals(dataSourceClass)) {
                return dataSource;
            }
        }

        throw new AuthenticationException("No datasource found for class: " + dataSourceClass);
    }
}
