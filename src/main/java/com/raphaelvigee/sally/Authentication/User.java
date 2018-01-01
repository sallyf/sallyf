package com.raphaelvigee.sally.Authentication;

public class User implements UserInterface<Integer>
{
    private Integer id;

    private String username;

    private String password;

    public User(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    @Override
    public Integer getId()
    {
        return id;
    }

    @Override
    public String getUsername()
    {
        return username;
    }

    @Override
    public String getPassword()
    {
        return password;
    }

    @Override
    public void setId(Integer id)
    {
        this.id = id;
    }

    @Override
    public void setUsername(String username)
    {
        this.username = username;
    }

    @Override
    public void setPassword(String password)
    {
        this.password = password;
    }
}
