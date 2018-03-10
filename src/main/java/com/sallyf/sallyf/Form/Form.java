package com.sallyf.sallyf.Form;

import com.sallyf.sallyf.Form.Exception.UnableToValidateException;
import com.sallyf.sallyf.Form.Type.FormType;
import com.sallyf.sallyf.Server.RuntimeBag;
import com.sallyf.sallyf.Server.RuntimeBagContext;
import com.sallyf.sallyf.Utils.MapUtils;
import org.eclipse.jetty.server.Request;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Form<T extends FormTypeInterface<O, ND>, O extends Options, ND>
{
    private String name;

    private FormBuilder<T, O, ND> formBuilder;

    private LinkedHashSet<Form> children;

    private Form parentForm;

    private O options;

    private ErrorsBag errorsBag;

    private ND normData;

    private Object modelData;

    private boolean submitted = false;

    public Form(String name, FormBuilder<T, O, ND> formBuilder, Form parentForm, O options)
    {
        this.name = name;
        this.formBuilder = formBuilder;
        this.parentForm = parentForm;
        this.options = options;
        this.children = new LinkedHashSet<>();
        this.errorsBag = new ErrorsBag();
    }

    public LinkedHashSet<Form> getChildren()
    {
        return children;
    }

    public Form getChild(String name)
    {
        for (Form child : children) {
            if (child.getName().equals(name)) {
                return child;
            }
        }

        return null;
    }

    public Set<Form> getMappedChildren()
    {
        return getChildren()
                .stream()
                .filter(child -> child.getOptions().isMapped())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public void setChildren(LinkedHashSet<Form> children)
    {
        this.children = children;
    }

    public O getOptions()
    {
        return options;
    }

    public FormBuilder<T, O, ND> getBuilder()
    {
        return formBuilder;
    }

    public Form getParent()
    {
        return parentForm;
    }

    public FormView<T, O, ND> createView()
    {
        return createView(null);
    }

    public ErrorsBag getErrorsBag()
    {
        return errorsBag;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isSubmitted()
    {
        return submitted;
    }

    public void setSubmitted(boolean submitted)
    {
        this.submitted = submitted;
    }

    public String getFullName()
    {
        return getBuilder().getFullName();
    }

    public T getFormType()
    {
        return getBuilder().getFormType();
    }

    public ND getNormData()
    {
        return normData;
    }

    public Form getRoot()
    {
        Form current = this;

        while (true) {
            Form parent = current.getParent();

            if (parent == null) {
                return current;
            }

            current = parent;
        }
    }

    public void setNormData(ND normData)
    {
        this.normData = normData;
    }

    private <PT extends FormTypeInterface<PO, PD>, PO extends Options, PD> FormView<T, O, ND> createView(FormView<PT, PO, PD> parentView)
    {
        O resolvedVars = getBuilder().getFormType().createOptions();

        MapUtils.deepMerge(resolvedVars, getOptions());

        FormView<T, O, ND> formView = new FormView<>(getName(), this, parentView, resolvedVars);

        formView.setData(getNormData());

        getFormType().buildView(formView);

        getFormType().finishView(formView);

        LinkedHashSet<FormView> childrenFormViews = new LinkedHashSet<>();

        getChildren().forEach(childForm -> childrenFormViews.add(childForm.createView(formView)));

        formView.setChildren(childrenFormViews);

        return formView;
    }

    public void submit(Request request)
    {
        setNormData(getFormType().requestToNorm(this, request));

        getChildren().forEach(childForm -> childForm.submit(request));

        setModelData(getFormType().normToModel(this, getFormType().resolveData(this)));

        setSubmitted(true);
    }

    public void handleRequest()
    {
        RuntimeBag runtimeBag = RuntimeBagContext.get();

        Request request = runtimeBag.getRequest();

        Form<?, FormType.FormOptions, ?> root = getRoot();
        if (!root.getOptions().getMethod().equalsIgnoreCase(request.getMethod())) {
            return;
        }

        submit(request);

        validate();
    }

    private void validate()
    {
        for (ConstraintInterface constraint : getOptions().getConstraints()) {
            ErrorsBag errorsBag = getErrorsBag();

            try {
                constraint.validate(this, errorsBag);
            } catch (UnableToValidateException e) {
                errorsBag.addError(new ValidationError(String.format("Unable to validate %s for constraint %s", getModelData(), constraint)));
            }
        }

        for (Form child : getChildren()) {
            child.validate();
        }
    }

    public boolean isValid()
    {
        Supplier<Boolean> s = () -> {
            for (Form child : getChildren()) {
                if (!child.isValid()) {
                    return false;
                }
            }

            return true;
        };

        return !getErrorsBag().hasErrors() && s.get();
    }

    public Set<ValidationError> getErrors()
    {
        return getErrorsBag().getErrors();
    }

    public Object getModelData()
    {
        return modelData;
    }

    public void setModelData(Object modelData)
    {
        this.modelData = modelData;
    }
}
