package de.uhd.ifi.se.decision.management.confluence;

import de.uhd.ifi.se.decision.management.confluence.rest.DecisionKnowledgeElementKeeping;

import org.junit.Test;


import static org.junit.Assert.assertEquals;


public class MainPluginUnitTests
{
	 
	@Test
	public void testIsNullOrEmpty_false()
	{
		Boolean isEmpty= DecisionKnowledgeElementKeeping.isNullOrEmpty("abcd");
		assertEquals("Is not Empty!", false, isEmpty);
	}
	@Test
	public void testIsNullOrEmpty_true()
	{
		Boolean isEmpty= DecisionKnowledgeElementKeeping.isNullOrEmpty("");
		assertEquals("Is Empty!", true, isEmpty);
	}

	@Test
	public void testIsNullOrEmpty_NullTrue()
	{
		Boolean isEmpty= DecisionKnowledgeElementKeeping.isNullOrEmpty(null);
		assertEquals("Is null!", true, isEmpty);
	}
	
	
	
	

}