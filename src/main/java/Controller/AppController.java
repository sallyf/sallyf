package Controller;

import com.sallyf.sallyf.ActionInterface;
import com.sallyf.sallyf.BaseController;
import com.sallyf.sallyf.HTTPSession;
import com.sallyf.sallyf.Response;

public class AppController extends BaseController
{
    public ActionInterface helloWorldAction = (HTTPSession session) -> {
        return new Response("Hello, " + session.getParms().get("name"));
    };
}
