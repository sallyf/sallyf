package com.sallyf.sallyf.ContainerInstantiator;

public class ActivityTracker
{
    private boolean changed = false;

    public void apply(boolean v)
    {
        if (v) {
            changed = true;
        }
    }

    public boolean isChanged()
    {
        return changed;
    }
}
