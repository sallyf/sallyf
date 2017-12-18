//package com.raphaelvigee.sally.Server;
//
//import com.raphaelvigee.sally.Container.Container;
//import com.raphaelvigee.sally.Container.ContainerAwareInterface;
//import com.raphaelvigee.sally.Event.RequestEvent;
//import com.raphaelvigee.sally.Event.ResponseEvent;
//import com.raphaelvigee.sally.Event.RouteMatchEvent;
//import com.raphaelvigee.sally.EventDispatcher.EventDispatcher;
//import com.raphaelvigee.sally.KernelEvents;
//import com.raphaelvigee.sally.Router.Route;
//import com.raphaelvigee.sally.Router.Router;
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
//        com.raphaelvigee.sally.Server.Request request = (com.raphaelvigee.sally.Server.Request) servletRequest;
//        JettyResponse jettyResponse = (JettyResponse) servletResponse;
//
//        jettyResponse.applyResponse(handle(servletRequest, servletResponse));
//
//        request.setHandled(true);
//    }
//
//    private com.raphaelvigee.sally.Router.Response handle(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
//    {
//        com.raphaelvigee.sally.Server.Request request = (com.raphaelvigee.sally.Server.Request) servletRequest;
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
//                return new com.raphaelvigee.sally.Router.Response("Not Found", Status.NOT_FOUND, "text/plain");
//            }
//
//            com.raphaelvigee.sally.Router.Response response = route.getHandler().apply(request);
//
//            eventDispatcher.dispatch(KernelEvents.PRE_SEND_RESPONSE, new ResponseEvent(request, response));
//
//            return response;
//        } catch (Exception e) {
//            e.printStackTrace();
//
//            return new com.raphaelvigee.sally.Router.Response("Internal Error", Status.INTERNAL_ERROR, "text/plain");
//        }
//    }
//
//    @Override
//    public Container getContainer()
//    {
//        return container;
//    }
//}