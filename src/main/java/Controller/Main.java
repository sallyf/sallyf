package Controller;

import com.sallyf.sallyf.Container;
import com.sallyf.sallyf.Kernel;
import com.sallyf.sallyf.Routing;
import com.sallyf.sallyf.YServer;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        Kernel app = Kernel.newInstance();

        Container c = app.getContainer();

        c.add(YServer.class);
        c.add(Routing.class);
        c.add(AppController.class);

        c.get(Routing.class).get("/hello/{name}/{job}", c.get(AppController.class).helloWorldAction);
    }
}
