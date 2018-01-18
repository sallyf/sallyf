package com.sallyf.sallyf.FlashManager;

public class FlashEntry<D>
{
    private D data;

    public FlashEntry(D data)
    {
        this.data = data;
    }

    public D getData()
    {
        return data;
    }

    public void setData(D data)
    {
        this.data = data;
    }
}
