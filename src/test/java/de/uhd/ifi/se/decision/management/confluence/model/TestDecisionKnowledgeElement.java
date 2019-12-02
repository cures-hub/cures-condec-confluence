package de.uhd.ifi.se.decision.management.confluence.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import de.uhd.ifi.se.decision.management.confluence.model.impl.DecisionKnowledgeElementImpl;

public class TestDecisionKnowledgeElement {
	private DecisionKnowledgeElement element;

	@Before
	public void createElement() {
		element = new DecisionKnowledgeElementImpl();
		element.setDescription("myDescription");
		element.setId("myId");
		element.setKey("myKey");
		element.setLink("myLink");
		element.setMacroId("myMacroId");
		element.setPageId(1);
		element.setSummary("mySummary");
		element.setType("myType");
	}

	@Test
	public void testConstructor() {
		DecisionKnowledgeElement element = new DecisionKnowledgeElementImpl("", 0, "", "", "", "", "");
		assertNotNull(element);
	}

	@Test
	public void testDescription() {
		assertEquals("myDescription", element.getDescription());
	}

	@Test
	public void testId() {
		assertEquals("myId", element.getId());
	}

	@Test
	public void testKey() {
		assertEquals("myKey", element.getKey());
	}

	@Test
	public void testLink() {
		assertEquals("myLink", element.getLink());
	}

	@Test
	public void testMacroId() {
		assertEquals("myMacroId", element.getMacroId());
	}

	@Test
	public void testPageId() {
		assertEquals(1, element.getPageId());
	}

	@Test
	public void testSummary() {
		assertEquals("mySummary", element.getSummary());
	}

	@Test
	public void testType() {
		assertEquals("myType", element.getType());
	}

}