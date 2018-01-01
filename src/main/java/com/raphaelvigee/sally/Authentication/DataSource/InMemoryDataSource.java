package com.raphaelvigee.sally.Authentication.DataSource;

import com.raphaelvigee.sally.Authentication.UserDataSourceInterface;
import com.raphaelvigee.sally.Authentication.UserInterface;

import java.util.HashMap;
import java.util.Map;

public class InMemoryDataSource<U extends UserInterface<Integer>> implements UserDataSourceInterface<Integer>
{
    HashMap<Integer, U> users = new HashMap<>();

    @Override
    public UserInterface getUser(Integer id)
    {
        return users.get(id);
    }

    @Override
    public UserInterface getUser(String username, String password)
    {
        for (Map.Entry<Integer, U> entry : users.entrySet()) {
            U user = entry.getValue();

            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }

        return null;
    }

    public U addUser(U user)
    {
        user.setId(users.size());

        return users.put(user.getId(), user);
    }
}
