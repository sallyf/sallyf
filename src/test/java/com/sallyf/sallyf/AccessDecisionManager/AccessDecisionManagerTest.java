package com.sallyf.sallyf.AccessDecisionManager;

import com.sallyf.sallyf.Authentication.*;
import com.sallyf.sallyf.BaseFrameworkTest;
import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.PlainReference;
import com.sallyf.sallyf.Container.ServiceDefinition;
import com.sallyf.sallyf.EventDispatcher.EventDispatcher;
import com.sallyf.sallyf.Router.Router;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class AccessDecisionManagerTest extends BaseFrameworkTest
{
    private static final String ACCESS = "access";

    public AccessDecisionManager newInstance(Configuration configuration) throws Exception
    {
        Container c = new Container();
        c.add(new ServiceDefinition<>(NameVoter.class)).addTag(AccessDecisionManager.TAG_VOTER);
        c.add(new ServiceDefinition<>(CapitalLettersVoter.class)).addTag(AccessDecisionManager.TAG_VOTER);
        c.add(new ServiceDefinition<>(ContainsAVoter.class)).addTag(AccessDecisionManager.TAG_VOTER);
        c.add(new ServiceDefinition<>(UncalledVoter.class)).addTag(AccessDecisionManager.TAG_VOTER);

        c.add(new ServiceDefinition<>(AccessDecisionManager.class));

        c.instantiate();

        return c.get(AccessDecisionManager.class);
    }

    private void run(Object subject, boolean eda, boolean edc, boolean edu) throws Exception
    {
        AccessDecisionManager authenticationManager = newInstance(new Configuration());

        boolean da = authenticationManager.vote(ACCESS, subject, null, DecisionStrategy.AFFIRMATIVE);
        boolean dc = authenticationManager.vote(ACCESS, subject, null, DecisionStrategy.CONSENSUS);
        boolean du = authenticationManager.vote(ACCESS, subject, null, DecisionStrategy.UNANIMOUS);

        assertEquals("Affirmative", eda, da);
        assertEquals("Consensus", edc, dc);
        assertEquals("Unanimous", edu, du);
    }

    @Test
    public void test1() throws Exception
    {
        run("admin", true, true, false);
    }

    @Test
    public void test2() throws Exception
    {
        run("foo", true, false, false);
    }

    @Test
    public void test3() throws Exception
    {
        run("FOO", true, true, false);
    }

    @Test
    public void test4() throws Exception
    {
        run("x", false, false, false);
    }

    @Test
    public void testUnsupportedSubject() throws Exception
    {
        run(new HashMap<>(), true, true, true);
    }
}
