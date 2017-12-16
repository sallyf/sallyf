package com.sallyf.sallyf;

import com.sallyf.sallyf.Exception.RouteDuplicateException;
import com.sallyf.sallyf.Router.*;
import com.sallyf.sallyf.Server.Request;
import com.sallyf.sallyf.Server.Method;
import fi.iki.elonen.NanoHTTPD;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

class CapitalizerResolver implements RouteParameterResolverInterface<String>
{
    @Override
    public boolean supports(String name, String value, Request request)
    {
        return Objects.equals(name, "name");
    }

    @Override
    public String resolve(String name, String value, Request request)
    {
        return value.toUpperCase();
    }
}

public class RouterTest
{
    @Test
    public void regexComputationTest()
    {
        Kernel app = Kernel.newInstance();

        Router router = app.getContainer().get(Router.class);

        Route route = new Route(Method.GET, "/hello/{foo}/{bar}/{dat_test}", (h) -> null);

        assertEquals("^/hello/([^/]*)/([^/]*)/([^/]*)$", route.getPath().getPattern());

        Request request = new Request();
        request.setMethod(NanoHTTPD.Method.GET);
        request.setUri("/hello/YOLO/hé/dat_var");

        RouteParameters expectedParameters = new RouteParameters();
        expectedParameters.put("foo", "YOLO");
        expectedParameters.put("bar", "hé");
        expectedParameters.put("dat_test", "dat_var");

        assertEquals(expectedParameters, router.getRouteParameters(route, request));
    }

    @Test
    public void routeMatcherTest() throws Exception
    {
        Route route1 = new Route(Method.GET, "/hello/{foo}/{bar}/{dat_test}", (h) -> null);
        Route route2 = new Route(Method.GET, "/qwertyuiop", (h) -> null);
        Route route3 = new Route(Method.POST, "/qwertyuiop", (h) -> null);
        Route route4 = new Route(Method.GET, "/", (h) -> null);

        Router router = new Router();
        router.addRoute(route1);
        router.addRoute(route2);
        router.addRoute(route3);
        router.addRoute(route4);

        Request request1 = new Request();
        request1.setMethod(NanoHTTPD.Method.POST);
        request1.setUri("/qwertyuiop");

        Route match1 = router.match(request1);

        assertEquals(route3, match1);

        Request request2 = new Request();
        request2.setMethod(NanoHTTPD.Method.GET);
        request2.setUri("/hello/YOLO/hé/dat_var");

        Route match2 = router.match(request2);

        assertEquals(route1, match2);

        Request request3 = new Request();
        request3.setMethod(NanoHTTPD.Method.GET);
        request3.setUri("/nop");

        Route match3 = router.match(request3);

        assertNull(match3);
    }

    @Test(expected = RouteDuplicateException.class)
    public void routeDuplicateExceptionTest() throws Exception
    {
        Route route1 = new Route(Method.GET, "/abc", (h) -> null);
        Route route2 = new Route(Method.GET, "/abc", (h) -> null);

        Router router = new Router();
        router.addRoute(route1);
        router.addRoute(route2);
    }

    @Test
    public void routeDuplicateTest() throws Exception
    {
        Route route1 = new Route(Method.GET, "/abc", (h) -> null);
        Route route2 = new Route(Method.POST, "/abc", (h) -> null);

        Router router = new Router();
        router.addRoute(route1);
        router.addRoute(route2);
    }

    @Test
    public void addControllerTest() throws Exception
    {
        Kernel app = Kernel.newInstance();

        Router router = app.getContainer().add(Router.class);

        router.addController(TestController.class);

        ArrayList<Route> routes = router.getRoutes();

        assertEquals(1, routes.size());

        Response response = routes.get(0).getHandler().apply(null);

        assertEquals("hello", response.getContent());
        assertEquals("/prefixed/hello", routes.get(0).getPath().getDeclaration());
    }

    @Test
    public void routeParameterResolverTest() throws Exception
    {
        Kernel app = Kernel.newInstance();

        Router router = app.getContainer().get(Router.class);

        router.addRouteParameterResolver(new CapitalizerResolver());

        Route route = new Route(Method.GET, "/{name}", (h) -> null);
        router.addRoute(route);

        Request request = new Request();
        request.setRoute(route);
        request.setUri("/lowercase");
        request.setMethod(NanoHTTPD.Method.GET);

        RouteParameters routeParameters = router.getRouteParameters(route, request);

        assertEquals("LOWERCASE", routeParameters.get("name"));
    }
}
