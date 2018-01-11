package com.sallyf.sallyf.Authentication;

import com.sallyf.sallyf.BaseFrameworkTest;
import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.PlainReference;
import com.sallyf.sallyf.Container.ServiceDefinition;
import com.sallyf.sallyf.EventDispatcher.EventDispatcher;
import com.sallyf.sallyf.Router.Router;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class AuthenticationVoterTest extends BaseFrameworkTest
{
    private static final String ACCESS = "access";

    public AuthenticationManager newInstance(Configuration configuration) throws Exception
    {
        Container c = new Container();
        c.add(new ServiceDefinition<>(NameVoter.class)).addTag(AuthenticationManager.TAG_VOTER);
        c.add(new ServiceDefinition<>(CapitalLettersVoter.class)).addTag(AuthenticationManager.TAG_VOTER);
        c.add(new ServiceDefinition<>(ContainsAVoter.class)).addTag(AuthenticationManager.TAG_VOTER);

        c.add(new ServiceDefinition<>(EventDispatcher.class));
        c.add(new ServiceDefinition<>(Router.class));
        c
                .add(new ServiceDefinition<>(AuthenticationManager.class))
                .setConfigurationReference(new PlainReference<>(configuration));

        c.instantiate();

        return c.get(AuthenticationManager.class);
    }

    private void run(Object subject, boolean eda, boolean edc, boolean edu) throws Exception
    {
        AuthenticationManager authenticationManager = newInstance(new Configuration());

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
