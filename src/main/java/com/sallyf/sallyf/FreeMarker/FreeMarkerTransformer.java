package com.sallyf.sallyf.FreeMarker;

import com.sallyf.sallyf.Exception.FrameworkException;
import com.sallyf.sallyf.Router.ResponseTransformerInterface;
import com.sallyf.sallyf.Server.RuntimeBag;
import freemarker.template.Template;

import java.io.StringWriter;

public class FreeMarkerTransformer implements ResponseTransformerInterface<FreeMarkerResponse, String>
{
    private FreeMarker freeMarker;

    public FreeMarkerTransformer(FreeMarker freeMarker)
    {
        this.freeMarker = freeMarker;
    }

    @Override
    public boolean supports(RuntimeBag runtimeBag, Object response)
    {
        return response instanceof FreeMarkerResponse;
    }

    @Override
    public String transform(RuntimeBag runtimeBag, FreeMarkerResponse response)
    {
        try {
            Template template = freeMarker.getConfiguration().getTemplate(response.getTemplate());

            StringWriter stringWriter = new StringWriter();
            template.process(response.getData(), stringWriter);

            return stringWriter.toString();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new FrameworkException(e);
        }
    }
}
