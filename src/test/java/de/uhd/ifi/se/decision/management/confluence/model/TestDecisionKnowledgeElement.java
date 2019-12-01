package de.uhd.ifi.se.decision.management.confluence.model;

import org.junit.Test;

import de.uhd.ifi.se.decision.management.confluence.model.impl.DecisionKnowledgeElementImpl;

import static org.junit.Assert.assertEquals;

import org.junit.Before;

public class TestDecisionKnowledgeElement
{
	DecisionKnowledgeElement de =new DecisionKnowledgeElementImpl();
	
	@Before
	public void createElement()
	{
		de.setDescription("myDescription");
		de.setGroup(123);
		de.setId("myId");
		de.setKey("myKey");
		de.setLink("myLink");
		de.setMacroId("myMacroId");
		de.setPageId(1);
		de.setSummary("mySummary");
		de.setType("myType");
	}
	
	@Test
	public void testDescription(){
		assertEquals("Description not equal!", "myDescription", de.getDescription());
	}
	@Test
	public void testGroup(){
		assertEquals("Group not equal!", 123, de.getGroup());
	}
	@Test
	public void testId(){
		assertEquals("Id not equal!", "myId", de.getId());
	}
	@Test
	public void testKey(){
		assertEquals("Key not equal!", "myKey", de.getKey());
	}
	@Test
	public void testLink(){
		assertEquals("Link not equal!", "myLink", de.getLink());
	}
	@Test
	public void testMacroId(){
		assertEquals("MacroId not equal!", "myMacroId", de.getMacroId());
	}
	@Test
	public void testPageId(){
		assertEquals("PageId not equal!", 1, de.getPageId());
	}	@Test
	public void testSummary(){
		assertEquals("Summary not equal!", "mySummary", de.getSummary());
	}	@Test
	public void testType(){
		assertEquals("Type not equal!", "myType", de.getType());
	}
	

}