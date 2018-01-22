package com.sallyf.sallyf.Form;

import com.sallyf.sallyf.Annotation.Route;
import com.sallyf.sallyf.Controller.BaseController;
import com.sallyf.sallyf.Form.Constraint.IsFalse;
import com.sallyf.sallyf.Form.Constraint.IsTrue;
import com.sallyf.sallyf.Form.Constraint.NotEmpty;
import com.sallyf.sallyf.Form.Type.*;
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
                .add("bar[]", TextType.class, () -> "bar 1")
                .add("submit", SubmitType.class, () -> "Hello !");

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
                }, () -> "true")
                .add("submit", SubmitType.class, (options) -> {
                    options.getAttributes().put("value", "Submit");
                }, () -> "Submit");

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
                }, () -> "yolo")
                .add("submit", SubmitType.class, (options) -> {
                }, () -> "Submit");

        form.build();

        if (request.getMethod().equalsIgnoreCase("post")) {
            Map<String, String[]> data = formManager.handleRequest(request, form);

            if (!form.getErrorsBag().hasErrors()) {
                return data.toString();
            }
        }

        return formManager.render(form);
    }

    @Route(path = "/checkboxes", methods = {Method.GET, Method.POST})
    public String checkboxes(Request request, FormManager formManager)
    {
        FormType form = FormBuilder
                .create()
                .add("cb1", CheckboxType.class, (options) -> {
                    options.getConstraints().add(new IsTrue("cb1 should be checked"));
                }, () -> true)
                .add("cb2", CheckboxType.class, (options) -> {
                    options.getConstraints().add(new IsFalse("cb2 should be unchecked"));
                }, () -> true)
                .add("cb3", CheckboxType.class, (options) -> {
                    options.getConstraints().add(new IsFalse("cb3 should be unchecked"));
                }, () -> false)
                .add("submit", SubmitType.class, () -> "Submit");

        form.build();

        if (request.getMethod().equalsIgnoreCase("post")) {
            Map<String, String[]> data = formManager.handleRequest(request, form);

            if (!form.getErrorsBag().hasErrors()) {
                return data.toString();
            }
        }

        return formManager.render(form);
    }

    @Route(path = "/textarea", methods = {Method.GET, Method.POST})
    public String textarea(Request request, FormManager formManager)
    {
        FormType form = FormBuilder
                .create()
                .add("ta", TextareaType.class, (options) -> {
                    options.getConstraints().add(new NotEmpty());
                }, () -> "Hello, World")
                .add("submit", SubmitType.class, () -> "Submit");

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
