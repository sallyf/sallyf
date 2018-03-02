package com.sallyf.sallyf.Form.Type;

import com.sallyf.sallyf.Form.Form;
import com.sallyf.sallyf.Form.Options;
import org.eclipse.jetty.server.Request;

public class PasswordType extends TextType
{
    @Override
    String getInputType()
    {
        return "password";
    }

    @Override
    public String requestToNorm(Form<?, Options, String> form, Request request)
    {
        return ""; // Prevent sending back password
    }
}
