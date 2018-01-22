package com.sallyf.sallyf.Form;

import com.sallyf.sallyf.Annotation.Route;
import com.sallyf.sallyf.Controller.BaseController;
import com.sallyf.sallyf.Form.Constraint.NotBlank;
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
                .add("foo[a][b][c][d]", TextType.class, (options) -> {
                    options.getConstraints().add(new NotBlank("The value \"{{value.0}}\" is blank"));
                })
                .add("bar[]", TextType.class, (options) -> {
                    options.getConstraints().add(new NotBlank());
                    options.getAttributes().put("value", "bar 1");
                })
                .add("submit", SubmitType.class, (options) -> {
                    options.getAttributes().put("value", "Hello !");
                });

        form.build();

        if (request.getMethod().equalsIgnoreCase("post")) {
            Map<String, String[]> data = formManager.handleRequest(request, form);

            if (!form.getErrorsBag().hasErrors()) {
                return data.toString();
            }
        }

        return formManager.render(form);
    }
}
