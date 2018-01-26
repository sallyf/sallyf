package com.sallyf.sallyf.Form;

import com.sallyf.sallyf.Annotation.Route;
import com.sallyf.sallyf.Controller.BaseController;
import com.sallyf.sallyf.Form.Constraint.IsFalse;
import com.sallyf.sallyf.Form.Constraint.IsTrue;
import com.sallyf.sallyf.Form.Constraint.NotEmpty;
import com.sallyf.sallyf.Form.Type.*;
import com.sallyf.sallyf.Server.Method;
import com.sallyf.sallyf.Utils.MapUtils;
import org.eclipse.jetty.server.Request;

import java.util.HashMap;
import java.util.Map;

public class TestController extends BaseController
{
    @Route(path = "/constraint-empty", methods = {Method.GET, Method.POST})
    public String constraintEmpty(Request request, FormManager formManager)
    {
        Map<String, Object> inData = MapUtils.parse("{bar: 'bar 1', submit: 'Hello !'}");

        Form<FormType, FormType.FormOptions, Object, Object> form = this.createFormBuilder(inData)
                .add("foo", TextType.class, (options) -> {
                    options.getConstraints().add(new NotEmpty("The value \"{{value}}\" is blank"));
                })
                .add("bar", TextType.class, (options) -> {
                })
                .add("submit", SubmitType.class, (options) -> {
                })
                .getForm();

        if (request.getMethod().equalsIgnoreCase("post")) {
            Map<String, Object> data = formManager.handleRequest(request, form);

            if (!form.getErrorsBag().hasErrors()) {
                return data.toString();
            }
        }

        return formManager.render(form.createView());
    }

    @Route(path = "/constraint-istrue-success", methods = {Method.GET, Method.POST})
    public String constrainIsTrueSuccess(Request request, FormManager formManager)
    {
        Map<String, Object> inData = MapUtils.parse("{test: 'true'}");

        Form<FormType, FormType.FormOptions, Object, Object> form = this.createFormBuilder(inData)
                .add("test", TextType.class, (options) -> {
                    options.getConstraints().add(new IsTrue());
                })
                .add("submit", SubmitType.class, (options) -> {
                    options.getAttributes().put("value", "Submit");
                })
                .getForm();

        if (request.getMethod().equalsIgnoreCase("post")) {
            Map<String, Object> data = formManager.handleRequest(request, form);

            if (!form.getErrorsBag().hasErrors()) {
                return data.toString();
            }
        }

        return formManager.render(form.createView());
    }

    @Route(path = "/constraint-isfalse-failure", methods = {Method.GET, Method.POST})
    public String constrainIsFalseFailure(Request request, FormManager formManager)
    {
        Map<String, Object> inData = MapUtils.parse("{test: 'yolo'}");

        Form<FormType, FormType.FormOptions, Object, Object> form = this.createFormBuilder(inData)
                .add("test", TextType.class, (options) -> {
                    options.getConstraints().add(new IsFalse());
                })
                .add("submit", SubmitType.class, (options) -> {
                })
                .getForm();

        if (request.getMethod().equalsIgnoreCase("post")) {
            Map<String, Object> data = formManager.handleRequest(request, form);

            if (!form.getErrorsBag().hasErrors()) {
                return data.toString();
            }
        }

        return formManager.render(form.createView());
    }

    @Route(path = "/checkboxes", methods = {Method.GET, Method.POST})
    public String checkboxes(Request request, FormManager formManager)
    {
        Map<String, Object> inData = MapUtils.parse("{cb1: true, cb2: true}");

        Form<FormType, FormType.FormOptions, Object, Object> form = this.createFormBuilder(inData)
                .add("cb1", CheckboxType.class, (options) -> {
                    options.getConstraints().add(new IsTrue("cb1 should be checked"));
                })
                .add("cb2", CheckboxType.class, (options) -> {
                    options.getConstraints().add(new IsFalse("cb2 should be unchecked"));
                })
                .add("cb3", CheckboxType.class, (options) -> {
                    options.getConstraints().add(new IsFalse("cb3 should be unchecked"));
                })
                .add("submit", SubmitType.class)
                .getForm();

        if (request.getMethod().equalsIgnoreCase("post")) {
            Map<String, Object> data = formManager.handleRequest(request, form);

            if (!form.getErrorsBag().hasErrors()) {
                return data.toString();
            }
        }

        return formManager.render(form.createView());
    }

    @Route(path = "/textarea", methods = {Method.GET, Method.POST})
    public String textarea(Request request, FormManager formManager)
    {
        Map<String, Object> inData = MapUtils.parse("{ta: 'Hello, World'}");

        Form<FormType, FormType.FormOptions, Object, Object> form = this.createFormBuilder(inData)
                .add("ta", TextareaType.class, (options) -> {
                    options.getConstraints().add(new NotEmpty());
                })
                .add("submit", SubmitType.class)
                .getForm();

        if (request.getMethod().equalsIgnoreCase("post")) {
            Map<String, Object> data = formManager.handleRequest(request, form);

            if (!form.getErrorsBag().hasErrors()) {
                return data.toString();
            }
        }

        return formManager.render(form.createView());
    }
}
