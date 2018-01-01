package com.sallyf.sallyf.Authentication;

public interface UserDataSourceInterface<ID>
{
    UserInterface getUser(ID id);

    UserInterface getUser(String username, String password);
}
