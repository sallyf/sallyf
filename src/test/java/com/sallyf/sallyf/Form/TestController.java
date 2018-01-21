package com.sallyf.sallyf.Form;

import com.sallyf.sallyf.Annotation.Route;
import com.sallyf.sallyf.Controller.BaseController;
import com.sallyf.sallyf.Form.Type.FormType;
import com.sallyf.sallyf.Form.Type.SubmitType;
import com.sallyf.sallyf.Form.Type.TextType;
import com.sallyf.sallyf.Server.Method;
import org.eclipse.jetty.server.Request;

import java.util.Map;

public class TestController extends BaseController
{
    @Route(path = "/simple-form", methods = {Method.GET, Method.POST})
    public String simpleForm(Request request, FormManager formManager)
    {
        FormType form = FormBuilder
                .create((options) -> {
                    options.getAttributes().put("action", "/simple-form");
                })
                .add(TextType.class, (options) -> {
                    options.getAttributes().put("name", "foo[a][b][c][d]");
                })
                .add(TextType.class, (options) -> {
                    options.getAttributes().put("name", "foo[a][b][c][e]");
                })
                .add(SubmitType.class, (options) -> {
                    options.getAttributes().put("value", "Hello !");
                });

        if (request.getMethod().equalsIgnoreCase("post")) {
            Map<String, Object> data = formManager.handleRequest(request, form);

            return data.toString();
        }

        return formManager.render(form);
    }
}
