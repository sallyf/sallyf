package com.sallyf.sallyf.Server;

public class RuntimeBagContext
{
    public static RuntimeBag get()
    {
        return ThreadAttributes.get("_runtime_bag");
    }
}
