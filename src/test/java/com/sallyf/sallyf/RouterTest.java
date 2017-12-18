package com.sallyf.sallyf;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Exception.FrameworkException;
import com.sallyf.sallyf.Exception.RouteDuplicateException;
import com.sallyf.sallyf.Router.*;
import com.sallyf.sallyf.Server.Method;
import com.sallyf.sallyf.Server.RuntimeBag;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.http.HttpURI;
import org.eclipse.jetty.http.MetaData;
import org.eclipse.jetty.server.Request;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

class CapitalizerResolver implements RouteParameterResolverInterface<String>
{
    @Override
    public boolean supports(String name, String value, RuntimeBag runtimeBag)
    {
        return Objects.equals(name, "name");
    }

    @Override
    public String resolve(String name, String value, RuntimeBag runtimeBag)
    {
        return value.toUpperCase();
    }
}

public class RouterTest
{
    @Test
    public void regexComputationTest() throws FrameworkException
    {
        Kernel app = Kernel.newInstance();

        Router router = app.getContainer().get(Router.class);

        Route route = new Route(Method.GET, "/hello/{foo}/{bar}/{dat_test}", (rb) -> null);

        assertEquals("^/hello/([^/]*)/([^/]*)/([^/]*)$", route.getPath().getPattern());

        Request request = new Request(null, null);
        request.setMethod(Method.GET.toString());
        request.setPathInfo("/hello/YOLO/hé/dat_var");

        RouteParameters expectedParameters = new RouteParameters();
        expectedParameters.put("foo", "YOLO");
        expectedParameters.put("bar", "hé");
        expectedParameters.put("dat_test", "dat_var");

        assertEquals(expectedParameters, router.getRouteParameters(new RuntimeBag(request, route)));

        app.stop();
    }

    @Test
    public void routeMatcherTest() throws Exception
    {
        Route route1 = new Route(Method.GET, "/hello/{foo}/{bar}/{dat_test}", (rb) -> null);
        Route route2 = new Route(Method.GET, "/qwertyuiop", (rb) -> null);
        Route route3 = new Route(Method.POST, "/qwertyuiop", (rb) -> null);
        Route route4 = new Route(Method.GET, "/", (rb) -> null);

        Container container = new Container();

        Router router = new Router(container);
        router.addRoute(route1);
        router.addRoute(route2);
        router.addRoute(route3);
        router.addRoute(route4);

        Request request1 = new Request(null, null);
        MetaData.Request httpFields1 = new MetaData.Request(new HttpFields());
        httpFields1.setURI(new HttpURI("/qwertyuiop"));
        httpFields1.setMethod(Method.POST.toString());
        request1.setMetaData(httpFields1);

        Route match1 = router.match(request1);

        assertEquals(route3, match1);

        Request request2 = new Request(null, null);
        MetaData.Request httpFields2 = new MetaData.Request(new HttpFields());
        httpFields2.setURI(new HttpURI("/hello/YOLO/hé/dat_var"));
        httpFields2.setMethod(Method.GET.toString());
        request2.setMetaData(httpFields2);

        Route match2 = router.match(request2);

        assertEquals(route1, match2);

        Request request3 = new Request(null, null);
        MetaData.Request httpFields3 = new MetaData.Request(new HttpFields());
        httpFields3.setURI(new HttpURI("/nop"));
        httpFields3.setMethod(Method.GET.toString());
        request3.setMetaData(httpFields3);

        Route match3 = router.match(request3);

        assertNull(match3);
    }

    @Test(expected = RouteDuplicateException.class)
    public void routeDuplicateExceptionTest() throws Exception
    {
        Route route1 = new Route(Method.GET, "/abc", (rb) -> null);
        Route route2 = new Route(Method.GET, "/abc", (rb) -> null);

        Container container = new Container();

        Router router = new Router(container);
        router.addRoute(route1);
        router.addRoute(route2);
    }

    @Test
    public void routeDuplicateTest() throws Exception
    {
        Route route1 = new Route(Method.GET, "/abc", (rb) -> null);
        Route route2 = new Route(Method.POST, "/abc", (rb) -> null);

        Container container = new Container();

        Router router = new Router(container);
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

        app.stop();
    }

    @Test
    public void routeParameterResolverTest() throws Exception
    {
        Kernel app = Kernel.newInstance();

        Router router = app.getContainer().get(Router.class);

        router.addRouteParameterResolver(new CapitalizerResolver());

        Route route = new Route(Method.GET, "/{name}", (rb) -> null);
        router.addRoute(route);

        Request request = new Request(null, null);
        request.setPathInfo("/lowercase");
        request.setMethod(Method.GET.toString());

        RouteParameters routeParameters = router.getRouteParameters(new RuntimeBag(request, route));

        assertEquals("LOWERCASE", routeParameters.get("name"));
    }
}
