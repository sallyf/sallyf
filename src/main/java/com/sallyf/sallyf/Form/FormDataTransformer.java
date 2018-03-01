package com.sallyf.sallyf.Form;

import org.eclipse.jetty.server.Request;

public interface FormDataTransformer<O extends Options, ND>
{
    ND modelToNorm(Form<?, O, ND> form, Object data);

    Object normToView(Form<?, O, ND> form, ND data);
}
