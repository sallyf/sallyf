package com.sallyf.sallyf.Util;

import com.sallyf.sallyf.Router.Response;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Jetty
{
    public static void applyResponse(HttpServletResponse servletResponse, Response response)
    {
        servletResponse.setContentType(response.getMimeType());
        servletResponse.setStatus(response.getStatus().getRequestStatus());
        try {
            servletResponse.getWriter().print(response.getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
