package de.uhd.ifi.se.decision.management.confluence.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class TestKnowledgeElement {
	private KnowledgeElement element;

	@Before
	public void createElement() {
		element = new KnowledgeElement();
		element.setDescription("myDescription");
		element.setId("myId");
		element.setKey("myKey");
		element.setLink("myLink");
		element.setMacroId("myMacroId");
		element.setPageId(1);
		element.setSummary("mySummary");
		element.setType("myType");
		element.setCreator("myAuthor");
		element.setUpdatingDate("0");
	}

	@Test
	public void testConstructor() {
		KnowledgeElement element = new KnowledgeElement("", 0, "", "", "", "", "");
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

	@Test
	public void testStatus() {
		assertEquals("undefined", element.getStatus());
		assertEquals("black", element.getStatusColor());

		element.setStatus("unresolved");
		assertEquals("unresolved", element.getStatus());
		assertEquals("crimson", element.getStatusColor());

		element.setStatus("rejected");
		assertEquals("rejected", element.getStatus());
		assertEquals("gray", element.getStatusColor());
	}

	@Test
	public void testCreator() {
		assertEquals("myAuthor", element.getCreator());
	}

	@Test
	public void testUpdatingDate() {
		assertEquals("1970-01-01", element.getUpdatingDate());
	}
}