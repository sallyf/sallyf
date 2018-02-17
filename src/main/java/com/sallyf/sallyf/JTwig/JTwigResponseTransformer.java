package com.sallyf.sallyf.JTwig;

import com.sallyf.sallyf.Router.ResponseTransformerInterface;
import com.sallyf.sallyf.Server.RuntimeBag;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

public class JTwigResponseTransformer implements ResponseTransformerInterface<JTwigResponse, String>
{
    private JTwig jtwig;

    public JTwigResponseTransformer(JTwig jtwig)
    {
        this.jtwig = jtwig;
    }

    @Override
    public boolean supports(RuntimeBag runtimeBag, Object response)
    {
        return response instanceof JTwigResponse;
    }

    @Override
    public String transform(RuntimeBag runtimeBag, JTwigResponse response)
    {
        JtwigTemplate jtwigTemplate = JtwigTemplate.classpathTemplate(response.getTemplate(), jtwig.getConfiguration());

        JtwigModel model = JtwigModel.newModel(response.getData())
                .with("_", runtimeBag)
                .with("runtimeBag", runtimeBag);

        return jtwigTemplate.render(model);
    }
}
