package com.sallyf.sallyf.Form;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ServiceDefinition;
import com.sallyf.sallyf.Form.Type.FormType;
import com.sallyf.sallyf.Form.Type.SubmitType;
import com.sallyf.sallyf.Form.Type.TextType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FormTest
{
    private FormManager formManager;

    @Before
    public void setUp() throws Exception
    {
        Container c = new Container();

        c.add(new ServiceDefinition<>(FormManager.class));

        c.instantiate();

        formManager = c.get(FormManager.class);
    }

    @Test
    public void createFormTest()
    {
        FormType form = FormBuilder.create();

        form.add(TextType.class);
        form.add(SubmitType.class, (options) -> {
            options.getAttributes().put("value", "Hello !");
        });

        String formView = formManager.render(form);

        String expected = "<form method=\"post\"><input type=\"text\" value=\"\"><input type=\"submit\" value=\"Hello !\"></form>";

        assertEquals(expected, formView);
    }
}
