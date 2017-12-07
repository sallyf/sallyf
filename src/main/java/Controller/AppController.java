package Controller;

import com.raphaelvigee.sally.ActionInterface;
import com.raphaelvigee.sally.BaseController;
import com.raphaelvigee.sally.HTTPSession;
import com.raphaelvigee.sally.Response;

public class AppController extends BaseController
{
    public ActionInterface helloWorldAction = (HTTPSession session) -> {
        return new Response("Hello, " + session.getParms().get("name"));
    };
}
