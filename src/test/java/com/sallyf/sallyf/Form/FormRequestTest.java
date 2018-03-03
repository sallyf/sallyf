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

        HtmlTextInput textField = form.getInputByName("foo");
        textField.setValueAttribute("bar");

        HtmlSubmitInput button = form.getInputByName("submit");

        HtmlPage page2 = button.click();

        String v = page2.getBody().getTextContent();

        Assert.assertEquals("{foo=bar, bar=bar 1, submit=Hello !}", v);
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

        String v1 = page1.getWebResponse().getContentAsString();

        HtmlForm form = page1.getForms().get(0);
        HtmlSubmitInput button = form.getInputByName("submit");

        HtmlPage page2 = button.click();

        String v = page2.getBody().getTextContent();

        String v2 = page2.getWebResponse().getContentAsString();

        Assert.assertEquals("{test=true, submit=Submit}", v);
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

    @Test
    public void testTextarea() throws Exception
    {
        WebClient webClient = new WebClient();

        HtmlPage page1 = webClient.getPage(getRootURL() + "/textarea");

        HtmlForm form = page1.getForms().get(0);
        HtmlSubmitInput button = form.getInputByName("submit");

        HtmlPage page2 = button.click();

        String v = page2.getBody().getTextContent();

        Assert.assertEquals("{ta=Hello, World, submit=submit}", v);
    }

    @Test
    public void testChoices() throws Exception
    {
        WebClient webClient = new WebClient();

        HtmlPage page1 = webClient.getPage(getRootURL() + "/choices");

        HtmlForm form = page1.getForms().get(0);

        HtmlSelect selectSingle = page1.getHtmlElementById("select-single");

        HtmlOption selectSingleOption = selectSingle.getOption(1);
        Assert.assertEquals("Two", selectSingleOption.asText());
        Assert.assertEquals("TWO", selectSingleOption.getValueAttribute());

        Assert.assertEquals(2, selectSingle.getSelectedIndex());

        selectSingle.setSelectedIndex(1);

        HtmlSelect selectMultiple = page1.getHtmlElementById("select-multiple");
        selectMultiple.getOption(0).setSelected(true);
        selectMultiple.getOption(2).setSelected(true);

        HtmlRadioButtonInput radio = page1.getHtmlElementById("radio-two");
        radio.setChecked(true);

        HtmlCheckBoxInput cbOne = page1.getHtmlElementById("cb-one");
        cbOne.setChecked(true);

        HtmlCheckBoxInput cbTwo = page1.getHtmlElementById("cb-two");
        cbTwo.setChecked(true);

        HtmlSubmitInput button = form.getInputByName("submit");

        HtmlPage page2 = button.click();

        String v = page2.getBody().getTextContent();

        Assert.assertEquals("{select-single=two, select-multiple=[one, three], radios=two, checkboxes=[one, two], submit=submit}", v);
    }
}
