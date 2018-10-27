package ut.com.atlassian.tutorial;

import org.junit.Test;
import de.uhd.ifi.se.decision.api.MyPluginComponent;
import de.uhd.ifi.se.decision.impl.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}