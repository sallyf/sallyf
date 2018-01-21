package com.sallyf.sallyf.Form;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.sallyf.sallyf.BaseFrameworkTest;
import com.sallyf.sallyf.Container.Exception.ServiceInstantiationException;
import com.sallyf.sallyf.Container.ServiceDefinition;
import org.junit.Assert;
import org.junit.Test;

public class FormRequestTest extends BaseFrameworkTest
{
    @Override
    public void preBoot() throws ServiceInstantiationException
    {
        app.getContainer().add(new ServiceDefinition<>(FormManager.class));
    }

    @Override
    public void setUp() throws Exception
    {
        setUp(TestController.class);
    }

    @Test
    public void testSubmit() throws Exception
    {
        WebClient webClient = new WebClient();

        HtmlPage page1 = webClient.getPage(getRootURL() + "/simple-form");

        HtmlForm form = page1.getForms().get(0);

        HtmlTextInput textField = form.getInputByName("foo[a][b][c][d]");
        textField.setValueAttribute("bar");

        HtmlSubmitInput button = form.getInputByName("submit");

        HtmlPage page2 = button.click();

        String v = page2.getBody().getTextContent();

        Assert.assertEquals("{foo[a][b][c][d]=[bar],submit=[Hello !],bar[]=[bar 1]}", v);
    }

}
