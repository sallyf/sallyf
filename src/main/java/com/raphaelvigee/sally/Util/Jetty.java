package com.raphaelvigee.sally.Util;

import com.raphaelvigee.sally.Router.RedirectResponse;
import com.raphaelvigee.sally.Router.Response;
import com.raphaelvigee.sally.Server.Status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

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

        for (Map.Entry<String, ArrayList<String>> entry : response.getHeaders().entrySet()) {
            String name = entry.getKey();
            ArrayList<String> headers = entry.getValue();
            for (String values : headers) {
                servletResponse.addHeader(name, values);
            }
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
