package Controller;

import com.raphaelvigee.sally.*;

public class AppController extends BaseController
{
    public ActionInterface helloWorldAction = (HTTPSession session, Route route) -> {
        RouteParameters parameters = route.getParameters(session);
        String name = parameters.get("name");
        String job = parameters.get("job");

        return new Response("Hello, " + name + " you have the position of: " + job);
    };
}
