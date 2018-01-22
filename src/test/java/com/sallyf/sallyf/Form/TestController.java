package com.sallyf.sallyf.Form;

import com.sallyf.sallyf.Annotation.Route;
import com.sallyf.sallyf.Controller.BaseController;
import com.sallyf.sallyf.Form.Constraint.IsFalse;
import com.sallyf.sallyf.Form.Constraint.IsTrue;
import com.sallyf.sallyf.Form.Constraint.NotEmpty;
import com.sallyf.sallyf.Form.Type.FormType;
import com.sallyf.sallyf.Form.Type.SubmitType;
import com.sallyf.sallyf.Form.Type.TextType;
import com.sallyf.sallyf.Server.Method;
import org.eclipse.jetty.server.Request;

import java.util.Map;

public class TestController extends BaseController
{
    @Route(path = "/constraint-empty", methods = {Method.GET, Method.POST})
    public String constraintEmpty(Request request, FormManager formManager)
    {
        FormType form = FormBuilder
                .create((options) -> {
                    options.getAttributes().put("action", "/constraint-empty");
                })
                .add("foo[a][b][c][d]", TextType.class, (options) -> {
                    options.getConstraints().add(new NotEmpty("The value \"{{value}}\" is blank"));
                })
                .add("bar[]", TextType.class, (options) -> {
                    options.getConstraints().add(new NotEmpty());
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

    @Route(path = "/constraint-istrue-success", methods = {Method.GET, Method.POST})
    public String constrainIsTrueSuccess(Request request, FormManager formManager)
    {
        FormType form = FormBuilder
                .create()
                .add("test", TextType.class, (options) -> {
                    options.getConstraints().add(new IsTrue());
                    options.getAttributes().put("value", "true");
                })
                .add("submit", SubmitType.class, (options) -> {
                    options.getAttributes().put("value", "Submit");
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

    @Route(path = "/constraint-isfalse-failure", methods = {Method.GET, Method.POST})
    public String constrainIsFalseFailure(Request request, FormManager formManager)
    {
        FormType form = FormBuilder
                .create()
                .add("test", TextType.class, (options) -> {
                    options.getConstraints().add(new IsFalse());
                    options.getAttributes().put("value", "yolo");
                })
                .add("submit", SubmitType.class, (options) -> {
                    options.getAttributes().put("value", "Submit");
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
