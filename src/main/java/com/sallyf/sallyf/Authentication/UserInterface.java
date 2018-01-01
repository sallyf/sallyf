package com.sallyf.sallyf.Authentication;

public interface UserInterface<ID>
{
    ID getId();

    String getUsername();

    String getPassword();

    void setId(ID id);

    void setUsername(String username);

    void setPassword(String password);
}
