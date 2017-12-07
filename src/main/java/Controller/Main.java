package Controller;

import com.raphaelvigee.sally.Container;
import com.raphaelvigee.sally.Kernel;
import com.raphaelvigee.sally.Routing;
import com.raphaelvigee.sally.YServer;

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
