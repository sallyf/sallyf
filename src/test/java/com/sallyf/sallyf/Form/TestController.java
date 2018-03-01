package com.sallyf.sallyf.Form;

import com.sallyf.sallyf.Annotation.Route;
import com.sallyf.sallyf.Controller.BaseController;
import com.sallyf.sallyf.Form.Constraint.IsFalse;
import com.sallyf.sallyf.Form.Constraint.IsTrue;
import com.sallyf.sallyf.Form.Constraint.NotEmpty;
import com.sallyf.sallyf.Form.Type.*;
import com.sallyf.sallyf.Server.Method;
import com.sallyf.sallyf.Utils.MapUtils;
import com.sallyf.sallyf.Utils.SetUtils;
import org.eclipse.jetty.server.Request;

import java.util.LinkedHashSet;
import java.util.Map;

public class TestController extends BaseController
{
    @Route(path = "/constraint-empty", methods = {Method.GET, Method.POST})
    public String constraintEmpty(Request request, FormManager formManager)
    {
        Map<String, Object> inData = MapUtils.parse("{bar: 'bar 1', submit: 'Hello !'}");

        Form<FormType, FormType.FormOptions, Object> form = this.createFormBuilder(inData)
                .add("foo", TextType.class, (options) -> {
                    options.getConstraints().add(new NotEmpty("`The value \"${value}\" is blank`"));
                })
                .add("bar", TextType.class, (options) -> {
                })
                .add("submit", SubmitType.class, (options) -> {
                })
                .getForm();

        form.handleRequest(request);

        if (form.isSubmitted() && form.isValid()) {
            return form.resolveData().toString();
        }

        return formManager.render(form.createView());
    }

    @Route(path = "/constraint-istrue-success", methods = {Method.GET, Method.POST})
    public String constrainIsTrueSuccess(Request request, FormManager formManager)
    {
        Map<String, Object> inData = MapUtils.parse("{test: 'true'}");

        Form<FormType, FormType.FormOptions, Object> form = this.createFormBuilder(inData)
                .add("test", TextType.class, (options) -> {
                    options.getConstraints().add(new IsTrue());
                })
                .add("submit", SubmitType.class, (options) -> {
                    options.getAttributes().put("value", "Submit");
                })
                .getForm();

        form.handleRequest(request);

        if (form.isSubmitted() && form.isValid()) {
            return form.resolveData().toString();
        }

        return formManager.render(form.createView());
    }

    @Route(path = "/constraint-isfalse-failure", methods = {Method.GET, Method.POST})
    public String constrainIsFalseFailure(Request request, FormManager formManager)
    {
        Map<String, Object> inData = MapUtils.parse("{test: 'yolo'}");

        Form<FormType, FormType.FormOptions, Object> form = this.createFormBuilder(inData)
                .add("test", TextType.class, (options) -> {
                    options.getConstraints().add(new IsFalse());
                })
                .add("submit", SubmitType.class, (options) -> {
                })
                .getForm();

        form.handleRequest(request);

        if (form.isSubmitted() && form.isValid()) {
            return form.resolveData().toString();
        }

        return formManager.render(form.createView());
    }

    @Route(path = "/checkboxes", methods = {Method.GET, Method.POST})
    public String checkboxes(Request request, FormManager formManager)
    {
        Map<String, Object> inData = MapUtils.parse("{cb1: true, cb2: true}");

        Form<FormType, FormType.FormOptions, Object> form = this.createFormBuilder(inData)
                .add("cb1", CheckboxType.class, (options) -> {
                    options.getConstraints().add(new IsTrue("`cb1 should be checked`"));
                })
                .add("cb2", CheckboxType.class, (options) -> {
                    options.getConstraints().add(new IsFalse("`cb2 should be unchecked`"));
                })
                .add("cb3", CheckboxType.class, (options) -> {
                    options.getConstraints().add(new IsFalse("`cb3 should be unchecked`"));
                })
                .add("submit", SubmitType.class)
                .getForm();

        form.handleRequest(request);

        if (form.isSubmitted() && form.isValid()) {
            return form.resolveData().toString();
        }

        return formManager.render(form.createView());
    }

    @Route(path = "/textarea", methods = {Method.GET, Method.POST})
    public String textarea(Request request, FormManager formManager)
    {
        Map<String, Object> inData = MapUtils.parse("{ta: 'Hello, World'}");

        Form<FormType, FormType.FormOptions, Object> form = this.createFormBuilder(inData)
                .add("ta", TextareaType.class, (options) -> {
                    options.getConstraints().add(new NotEmpty());
                })
                .add("submit", SubmitType.class)
                .getForm();

        form.handleRequest(request);

        if (form.isSubmitted() && form.isValid()) {
            return form.resolveData().toString();
        }

        return formManager.render(form.createView());
    }

    @Route(path = "/choices", methods = {Method.GET, Method.POST})
    public String choice(Request request, FormManager formManager)
    {
        Map<String, Object> inData = MapUtils.parse("{}");

        LinkedHashSet<String> choices = SetUtils.createLinkedHashSet("one", "two", "three");

        Form<FormType, FormType.FormOptions, Object> form = this.createFormBuilder(inData)
                .add("select-single", ChoiceType.class, (options) -> {
                    options.setChoices(choices);
                    options.setExpanded(false);
                    options.setMultiple(false);

                    options.getAttributes().put("id", "select-single");
                })
                .add("select-multiple", ChoiceType.class, (options) -> {
                    options.setChoices(choices);
                    options.setExpanded(false);
                    options.setMultiple(true);

                    options.getAttributes().put("id", "select-multiple");
                })
                .add("radios", ChoiceType.class, (options) -> {
                    options.setChoices(choices);
                    options.setExpanded(true);
                    options.setMultiple(false);

                    options.getAttributes().put("id", "radios");

                    options.setChoiceOptionsConsumer(childOptions -> {
                        childOptions.getAttributes().put("id", "radio-" + childOptions.getAttributes().get("value"));
                    });
                })
                .add("checkboxes", ChoiceType.class, (options) -> {
                    options.setChoices(choices);
                    options.setExpanded(true);
                    options.setMultiple(true);

                    options.setChoiceOptionsConsumer(childOptions -> {
                        childOptions.getAttributes().put("id", "cb-" + childOptions.getAttributes().get("value"));
                    });
                })
                .add("submit", SubmitType.class)
                .getForm();

        form.handleRequest(request);

        if (form.isSubmitted() && form.isValid()) {
            return form.resolveData().toString();
        }

        return formManager.render(form.createView());
    }
}
