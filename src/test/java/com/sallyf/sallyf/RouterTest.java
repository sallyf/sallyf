package com.sallyf.sallyf;

import com.sallyf.sallyf.Exception.RouteDuplicateException;
import com.sallyf.sallyf.Router.Response;
import com.sallyf.sallyf.Router.Route;
import com.sallyf.sallyf.Router.RouteParameters;
import com.sallyf.sallyf.Router.Router;
import com.sallyf.sallyf.Server.HTTPSession;
import com.sallyf.sallyf.Server.Method;
import fi.iki.elonen.NanoHTTPD;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class RouterTest
{
    @Test
    public void regexComputationTest()
    {
        Router router = new Router();

        Route route = new Route(Method.GET, "/hello/{foo}/{bar}/{dat_test}", (c, h, r) -> null);

        assertEquals("^/hello/([^/]*)/([^/]*)/([^/]*)$", route.getPath().getPattern());

        HTTPSession session = new HTTPSession();
        session.setMethod(NanoHTTPD.Method.GET);
        session.setUri("/hello/YOLO/hé/dat_var");

        RouteParameters expectedParameters = new RouteParameters();
        expectedParameters.put("foo", "YOLO");
        expectedParameters.put("bar", "hé");
        expectedParameters.put("dat_test", "dat_var");

        assertEquals(expectedParameters, router.getRouteParameters(route, session));
    }

    @Test
    public void routeMatcherTest() throws Exception
    {
        Route route1 = new Route(Method.GET, "/hello/{foo}/{bar}/{dat_test}", (c, h, r) -> null);
        Route route2 = new Route(Method.GET, "/qwertyuiop", (c, h, r) -> null);
        Route route3 = new Route(Method.POST, "/qwertyuiop", (c, h, r) -> null);
        Route route4 = new Route(Method.GET, "/", (c, h, r) -> null);

        Router router = new Router();
        router.addRoute(route1);
        router.addRoute(route2);
        router.addRoute(route3);
        router.addRoute(route4);

        HTTPSession session1 = new HTTPSession();
        session1.setMethod(NanoHTTPD.Method.POST);
        session1.setUri("/qwertyuiop");

        Route match1 = router.match(session1);

        assertEquals(route3, match1);

        HTTPSession session2 = new HTTPSession();
        session2.setMethod(NanoHTTPD.Method.GET);
        session2.setUri("/hello/YOLO/hé/dat_var");

        Route match2 = router.match(session2);

        assertEquals(route1, match2);

        HTTPSession session3 = new HTTPSession();
        session3.setMethod(NanoHTTPD.Method.GET);
        session3.setUri("/nop");

        Route match3 = router.match(session3);

        assertNull(match3);
    }

    @Test(expected = RouteDuplicateException.class)
    public void routeDuplicateExceptionTest() throws Exception
    {
        Route route1 = new Route(Method.GET, "/abc", (c, h, r) -> null);
        Route route2 = new Route(Method.GET, "/abc", (c, h, r) -> null);

        Router router = new Router();
        router.addRoute(route1);
        router.addRoute(route2);
    }

    @Test
    public void routeDuplicateTest() throws Exception
    {
        Route route1 = new Route(Method.GET, "/abc", (c, h, r) -> null);
        Route route2 = new Route(Method.POST, "/abc", (c, h, r) -> null);

        Router router = new Router();
        router.addRoute(route1);
        router.addRoute(route2);
    }

    @Test
    public void addControllerTest() throws Exception
    {
        Router router = new Router();

        router.addController(TestController.class);

        ArrayList<Route> routes = router.getRoutes();

        assertEquals(1, routes.size());

        Response response = routes.get(0).getHandler().apply(null, null, null);

        assertEquals("hello", response.getContent());
        assertEquals("/prefixed/hello", routes.get(0).getPath().getDeclaration());
    }
}
