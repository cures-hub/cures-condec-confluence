package de.uhd.ifi.se.decision.management.confluence;

import org.junit.Test;

import de.uhd.ifi.se.decision.management.confluence.api.MyPluginComponent;
import de.uhd.ifi.se.decision.management.confluence.impl.MyPluginComponentImpl;

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