package com.sallyf.sallyf.FlashManager;

import com.sallyf.sallyf.Annotation.Route;
import com.sallyf.sallyf.Controller.BaseController;

import java.util.Set;
import java.util.stream.Collectors;

public class TestController extends BaseController
{
    @Route(path = "/add-flashes")
    public String addFlash(FlashManagerHelper flashManager)
    {
        flashManager.addFlash(new FlashEntry<>("Flash 1"));
        flashManager.addFlash(new FlashEntry<>("Flash 2"));
        flashManager.addFlash(new FlashEntry<>("Flash 3"));

        return "OK";
    }

    @Route(path = "/forward")
    public String forward(FlashManagerHelper flashManager)
    {
        flashManager.setForward(true);

        return "OK";
    }

    @Route(path = "/read-flashes")
    public String readFlash(FlashManagerHelper flashManager)
    {
        Set<String> flashes = flashManager.getFlashes().stream().map(e -> (String) e.getData()).collect(Collectors.toSet());

        return String.join(" ", flashes);
    }
}