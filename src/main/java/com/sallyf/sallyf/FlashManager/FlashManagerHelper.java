package com.sallyf.sallyf.FlashManager;

import java.util.Set;

public class FlashManagerHelper
{
    private FlashManager flashManager;

    public FlashManagerHelper(FlashManager flashManager)
    {
        this.flashManager = flashManager;
    }

    public void addFlash(FlashEntry entry)
    {
        flashManager.addFlash(entry);
    }

    public Set<FlashEntry<?>> getFlashes()
    {
        return flashManager.getCurrentFlashes();
    }

    public boolean isForwarding()
    {
        return flashManager.isForwarding();
    }

    public void setForward(boolean value)
    {
        flashManager.setForward(value);
    }
}

