package com.sallyf.sallyf.AccessDecisionManager;

import com.sallyf.sallyf.BaseFrameworkTest;
import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ServiceDefinition;
import com.sallyf.sallyf.EventDispatcher.EventDispatcher;
import com.sallyf.sallyf.ExpressionLanguage.ExpressionLanguage;
import com.sallyf.sallyf.Router.Router;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class AccessDecisionManagerTest extends BaseFrameworkTest
{
    private static final String ACCESS = "access";

    public AccessDecisionManager newInstance()
    {
        Container c = new Container();
        c.add(new ServiceDefinition<>(NameVoter.class)).addTag(AccessDecisionManager.TAG_VOTER);
        c.add(new ServiceDefinition<>(CapitalLettersVoter.class)).addTag(AccessDecisionManager.TAG_VOTER);
        c.add(new ServiceDefinition<>(ContainsAVoter.class)).addTag(AccessDecisionManager.TAG_VOTER);
        c.add(new ServiceDefinition<>(UncalledVoter.class)).addTag(AccessDecisionManager.TAG_VOTER);

        c.add(new ServiceDefinition<>(EventDispatcher.class));
        c.add(new ServiceDefinition<>(Router.class));
        c.add(new ServiceDefinition<>(AccessDecisionManager.class));
        c.add(new ServiceDefinition<>(ExpressionLanguage.class));

        c.instantiate();

        return c.get(AccessDecisionManager.class);
    }

    private void run(Object subject, boolean eda, boolean edc, boolean edu)
    {
        AccessDecisionManager authenticationManager = newInstance();

        boolean dd = authenticationManager.vote(ACCESS, subject);

        boolean da = authenticationManager.vote(ACCESS, subject, DecisionStrategy.AFFIRMATIVE);
        boolean dc = authenticationManager.vote(ACCESS, subject, DecisionStrategy.CONSENSUS);
        boolean du = authenticationManager.vote(ACCESS, subject, DecisionStrategy.UNANIMOUS);

        assertEquals("Default", eda, dd);
        assertEquals("Affirmative", eda, da);
        assertEquals("Consensus", edc, dc);
        assertEquals("Unanimous", edu, du);
    }

    @Test
    public void test1()
    {
        run("admin", true, true, false);
    }

    @Test
    public void test2()
    {
        run("foo", true, false, false);
    }

    @Test
    public void test3()
    {
        run("FOO", true, true, false);
    }

    @Test
    public void test4()
    {
        run("x", false, false, false);
    }

    @Test
    public void testUnsupportedSubject()
    {
        run(new HashMap<>(), true, true, true);
    }
}
