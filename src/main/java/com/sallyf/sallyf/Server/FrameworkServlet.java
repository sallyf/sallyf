//package com.sallyf.sallyf.Server;
//
//import com.sallyf.sallyf.Container.Container;
//import com.sallyf.sallyf.Container.ContainerAwareInterface;
//import com.sallyf.sallyf.Event.RequestEvent;
//import com.sallyf.sallyf.Event.ResponseEvent;
//import com.sallyf.sallyf.Event.RouteMatchEvent;
//import com.sallyf.sallyf.EventDispatcher.EventDispatcher;
//import com.sallyf.sallyf.KernelEvents;
//import com.sallyf.sallyf.Router.Route;
//import com.sallyf.sallyf.Router.Router;
//
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//
//public class FrameworkServlet extends HttpServlet implements ContainerAwareInterface
//{
//    private Container container;
//
//    public FrameworkServlet(Container container)
//    {
//        this.container = container;
//    }
//
//    @Override
//    public void service(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws IOException
//    {
//        com.sallyf.sallyf.Server.Request request = (com.sallyf.sallyf.Server.Request) servletRequest;
//        JettyResponse jettyResponse = (JettyResponse) servletResponse;
//
//        jettyResponse.applyResponse(handle(servletRequest, servletResponse));
//
//        request.setHandled(true);
//    }
//
//    private com.sallyf.sallyf.Router.Response handle(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
//    {
//        com.sallyf.sallyf.Server.Request request = (com.sallyf.sallyf.Server.Request) servletRequest;
//
//        try {
//
//            EventDispatcher eventDispatcher = getContainer().get(EventDispatcher.class);
//
//            Router router = getContainer().get(Router.class);
//
//            eventDispatcher.dispatch(KernelEvents.PRE_MATCH_ROUTE, new RequestEvent(request));
//
//            Route route = router.match(request);
//            request.setRoute(route);
//
//            eventDispatcher.dispatch(KernelEvents.POST_MATCH_ROUTE, new RouteMatchEvent(request));
//
//            if (route == null) {
//                return new com.sallyf.sallyf.Router.Response("Not Found", Status.NOT_FOUND, "text/plain");
//            }
//
//            com.sallyf.sallyf.Router.Response response = route.getHandler().apply(request);
//
//            eventDispatcher.dispatch(KernelEvents.PRE_SEND_RESPONSE, new ResponseEvent(request, response));
//
//            return response;
//        } catch (Exception e) {
//            e.printStackTrace();
//
//            return new com.sallyf.sallyf.Router.Response("Internal Error", Status.INTERNAL_ERROR, "text/plain");
//        }
//    }
//
//    @Override
//    public Container getContainer()
//    {
//        return container;
//    }
//}