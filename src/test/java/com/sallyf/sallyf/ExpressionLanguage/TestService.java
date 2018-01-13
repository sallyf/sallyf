package com.sallyf.sallyf.ExpressionLanguage;

import com.sallyf.sallyf.Container.ContainerAwareInterface;
import com.sallyf.sallyf.Server.Status;

import java.util.ArrayList;
import java.util.HashMap;

public class TestService implements ContainerAwareInterface
{
    public boolean returnTrue()
    {
        return true;
    }

    public HashMap<String, ArrayList<Status>> returnHashMapOfArrayListOfStatus()
    {
        HashMap<String, ArrayList<Status>> hm = new HashMap<>();
        ArrayList<Status> a = new ArrayList<>();
        a.add(Status.OK);
        hm.put("statuses", a);

        return hm;
    }
}
