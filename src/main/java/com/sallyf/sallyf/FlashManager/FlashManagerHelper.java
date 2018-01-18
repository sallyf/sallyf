package com.sallyf.sallyf.FlashManager;

import com.sallyf.sallyf.Server.RuntimeBag;

import java.util.Set;

public class FlashManagerHelper
{
    private FlashManager flashManager;

    private RuntimeBag runtimeBag;

    public FlashManagerHelper(FlashManager flashManager, RuntimeBag runtimeBag)
    {
        this.flashManager = flashManager;
        this.runtimeBag = runtimeBag;
    }

    public void addFlash(FlashEntry entry)
    {
        flashManager.addFlash(runtimeBag, entry);
    }

    public Set<FlashEntry<?>> getFlashes()
    {
        return flashManager.getCurrentFlashes(runtimeBag);
    }

    public boolean isForwarding()
    {
        return flashManager.isForwarding(runtimeBag);
    }

    public void setForward(boolean value)
    {
        flashManager.setForward(runtimeBag, value);
    }
}

