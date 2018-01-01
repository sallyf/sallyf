package com.raphaelvigee.sally.Authentication;

import com.raphaelvigee.sally.Container.Container;
import com.raphaelvigee.sally.Container.ContainerAware;
import com.raphaelvigee.sally.Router.ActionParameterResolver.UserInterfaceResolver;
import com.raphaelvigee.sally.Router.Router;
import com.raphaelvigee.sally.Server.RuntimeBag;
import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

public class AuthenticationManager extends ContainerAware
{
    ArrayList<UserDataSourceInterface> dataSources = new ArrayList<>();

    public AuthenticationManager(Container container)
    {
        super(container);
    }

    public void initialize()
    {
        getContainer().get(Router.class).addActionParameterResolver(new UserInterfaceResolver(getContainer()));
    }

    public UserInterface authenticate(Request request, String username, String password) throws AuthenticationException
    {
        return authenticate(request, username, password, null);
    }

    public UserInterface authenticate(Request request, String username, String password, Class<? extends UserDataSourceInterface> dataSourceClass) throws AuthenticationException
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

        UserInterface user = dataSource.getUser(username, password);

        request.getSession(true).setAttribute("user", user);

        return user;
    }

    public void addDataSource(UserDataSourceInterface ds)
    {
        dataSources.add(ds);
    }

    public ArrayList<UserDataSourceInterface> getDataSources()
    {
        return dataSources;
    }

    public UserDataSourceInterface getDataSource(Class<? extends UserDataSourceInterface> dataSourceClass) throws AuthenticationException
    {
        for (UserDataSourceInterface dataSource : dataSources) {
            if (dataSource.getClass().equals(dataSourceClass)) {
                return dataSource;
            }
        }

        throw new AuthenticationException("No datasource found for class: " + dataSourceClass);
    }

    public UserInterface getUser(RuntimeBag runtimeBag)
    {
        Request request = runtimeBag.getRequest();

        HttpSession session = request.getSession();

        if (null == session) {
            return null;
        }

        return (UserInterface) session.getAttribute("user");
    }
}
