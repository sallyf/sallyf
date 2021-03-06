package com.sallyf.sallyf;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ServiceDefinition;
import com.sallyf.sallyf.EventDispatcher.EventDispatcher;
import com.sallyf.sallyf.Exception.FrameworkException;
import com.sallyf.sallyf.Router.*;
import com.sallyf.sallyf.Server.Method;
import com.sallyf.sallyf.Server.RuntimeBag;
import com.sallyf.sallyf.Server.ThreadAttributes;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.http.HttpURI;
import org.eclipse.jetty.http.MetaData;
import org.eclipse.jetty.server.Request;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class RouterTest
{
    @Test
    public void regexComputationTest() throws FrameworkException, InterruptedException
    {
        Kernel app = Kernel.newInstance();

        app.boot();

        Router router = app.getContainer().get(Router.class);

        Route route = new Route(new Method[]{Method.GET}, "/hello/{foo}/{bar}/{dat_test}", () -> null);
        Path path = route.getPath();
        path.getRequirements().put("foo", "(YOLO)");

        path.computePattern();

        assertEquals("^/hello/(YOLO)/([^/]*)/([^/]*)$", path.getPattern());

        Request request = new Request(null, null);
        request.setMethod(Method.GET.toString());
        request.setPathInfo("/hello/YOLO/hé/dat_var");

        RouteParameters expectedParameters = new RouteParameters();
        expectedParameters.put("foo", "YOLO");
        expectedParameters.put("bar", "hé");
        expectedParameters.put("dat_test", "dat_var");

        Thread thread = new Thread(() -> {
            ThreadAttributes.set("_runtime_bag", new RuntimeBag(request, route));
            assertEquals(expectedParameters, router.getRouteParameters());
        });

        thread.start();
        thread.join();

        app.stop();
    }

    @Test
    public void routeMatcherTest() throws Exception
    {
        Route route1 = new Route(new Method[]{Method.GET}, "/hello/{foo}/{bar}/{dat_test}", () -> null);
        Route route2 = new Route(new Method[]{Method.GET}, "/qwertyuiop", () -> null);
        Route route3 = new Route(new Method[]{Method.POST}, "/qwertyuiop", () -> null);
        Route route4 = new Route(new Method[]{Method.GET}, "/", () -> null);

        Container container = new Container();

        Router router = new Router(container, new EventDispatcher());
        router.registerRoute("route_1", route1);
        router.registerRoute("route_2", route2);
        router.registerRoute("route_3", route3);
        router.registerRoute("route_4", route4);

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

    @Test
    public void routeDuplicateTest() throws Exception
    {
        Route route1 = new Route(new Method[]{Method.GET}, "/abc", () -> null);
        Route route2 = new Route(new Method[]{Method.POST}, "/abc", () -> null);

        Container container = new Container();

        Router router = new Router(container, new EventDispatcher());
        router.registerRoute("route_1", route1);
        router.registerRoute("route_2", route2);
    }

    @Test
    public void addControllerTest() throws Exception
    {
        Kernel app = Kernel.newInstance();

        app.getContainer().add(new ServiceDefinition<>(CapitalizerResolver.class));

        app.boot();

        Router router = app.getContainer().get(Router.class);

        TestController testController = router.registerController(TestController.class);

        assertNotNull(testController);

        HashMap<String, Route> routes = router.getRoutes();

        assertEquals(8, routes.size());

        Route route = routes.get("test_hello_named");

        Response response = (Response) route.getHandler().apply();

        assertEquals("hello", response.getContent());
        assertEquals("/prefixed/hello", route.getPath().getDeclaration());

        app.stop();
    }
}
