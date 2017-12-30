package com.sallyf.sallyf.Util;

import com.sallyf.sallyf.Router.RedirectResponse;
import com.sallyf.sallyf.Router.Response;
import com.sallyf.sallyf.Server.Status;

import java.io.IOException;

public class Jetty
{
    public static void applyResponse(org.eclipse.jetty.server.Response servletResponse, Response response)
    {
        if (response instanceof RedirectResponse) {
            RedirectResponse redirectResponse = (RedirectResponse) response;

            try {
                servletResponse.sendRedirect(redirectResponse.getStatus().getRequestStatus(), redirectResponse.getTargetUrl());
            } catch (IOException e) {
                e.printStackTrace();
                Response error = new Response("Internal Error", Status.INTERNAL_ERROR, "text/plain");

                applyResponse(servletResponse, error);
                return;
            }
        }

        for (String n : response.getHeaderNames()) {
            servletResponse.addHeader(n, response.getHeader(n));
        }

        servletResponse.setContentType(response.getMimeType());
        servletResponse.setStatus(response.getStatus().getRequestStatus());

        try {
            servletResponse.getWriter().print(response.getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
