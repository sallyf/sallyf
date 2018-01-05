package com.sallyf.sallyf.Authentication;

import com.sallyf.sallyf.Container.ConfigurationInterface;

import java.util.ArrayList;

public class Configuration implements ConfigurationInterface
{
    public ArrayList<UserDataSourceInterface> getDataSources()
    {
        return new ArrayList<>();
    }
}
