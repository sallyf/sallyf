package com.sallyf.sallyf.Form;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
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
    public void testNoConstraints() throws Exception
    {
        WebClient webClient = new WebClient();

        HtmlPage page1 = webClient.getPage(getRootURL() + "/constraint-empty");

        HtmlForm form = page1.getForms().get(0);

        HtmlTextInput textField = form.getInputByName("foo[a][b][c][d]");
        textField.setValueAttribute("bar");

        HtmlSubmitInput button = form.getInputByName("submit");

        HtmlPage page2 = button.click();

        String v = page2.getBody().getTextContent();

        Assert.assertEquals("{foo[a][b][c][d]=[bar],submit=[Hello !],bar[]=[bar 1]}", v);
    }

    @Test
    public void testConstraintNotEmpty() throws Exception
    {
        WebClient webClient = new WebClient();

        HtmlPage page1 = webClient.getPage(getRootURL() + "/constraint-empty");

        HtmlForm form = page1.getForms().get(0);

        HtmlSubmitInput button = form.getInputByName("submit");

        HtmlPage page2 = button.click();

        String content = page2.getWebResponse().getContentAsString();

        Assert.assertTrue(content.contains("The value \"\" is blank"));
    }

    @Test
    public void testConstraintIsTrueSuccess() throws Exception
    {
        WebClient webClient = new WebClient();

        HtmlPage page1 = webClient.getPage(getRootURL() + "/constraint-istrue-success");

        HtmlForm form = page1.getForms().get(0);
        HtmlSubmitInput button = form.getInputByName("submit");

        HtmlPage page2 = button.click();

        String v = page2.getBody().getTextContent();

        Assert.assertEquals("{test=[true],submit=[Submit]}", v);
    }

    @Test
    public void testConstraintIsFalseFailure() throws Exception
    {
        WebClient webClient = new WebClient();

        HtmlPage page1 = webClient.getPage(getRootURL() + "/constraint-isfalse-failure");

        HtmlForm form = page1.getForms().get(0);
        HtmlSubmitInput button = form.getInputByName("submit");

        HtmlPage page2 = button.click();

        String content = page2.getWebResponse().getContentAsString();

        Assert.assertTrue(content.contains("\"yolo\" is not false"));
    }

    @Test
    public void testConstraintCheckboxes() throws Exception
    {
        WebClient webClient = new WebClient();

        HtmlPage page1 = webClient.getPage(getRootURL() + "/checkboxes");

        HtmlForm form = page1.getForms().get(0);

        HtmlSubmitInput button = form.getInputByName("submit");

        HtmlPage page2 = button.click();

        String content = page2.getWebResponse().getContentAsString();

        Assert.assertFalse(content.contains("cb1 should be checked"));
        Assert.assertTrue(content.contains("cb2 should be unchecked"));
        Assert.assertFalse(content.contains("cb3 should be unchecked"));
    }
}
