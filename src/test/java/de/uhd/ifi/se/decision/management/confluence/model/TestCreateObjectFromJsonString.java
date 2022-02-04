package de.uhd.ifi.se.decision.management.confluence.model;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class TestCreateObjectFromJsonString {

	@Test
	public void testParseJsonString() {
		String jsonString = "[{\"link\":\"https://jira-se.ifi.uni-heidelberg.de/browse/ISE2019-103\",\"pageId\":122191874,\"summary\":\"link "
				+ "type to devices\",\"type\":\"Sub-task\",\"key\":\"ISE2019-103\",\"id\":\"1911281118291129ISE2019-103\", "
				+ "\"macroId\":\"28e02c88-0754-40de-9df1-ac403833dd38\",\"description\":\"Link "
				+ "new created type to different devices inside the pop up field.\"}]";

		List<KnowledgeElement> elements = KnowledgeElement.parseJsonString(jsonString);
		KnowledgeElement element = elements.get(0);
		assertEquals("link type to devices", element.getSummary());
		assertEquals(122191874, element.getPageId());
		assertEquals("ISE2019-103", element.getKey());
		assertEquals("https://jira-se.ifi.uni-heidelberg.de/browse/ISE2019-103", element.getLink());
	}

	@Test
	public void testParseJsonStringArray() {
		String jsonString = "[{\"id\":56606,\"documentationLocation\":\"i\",\"summary\":\"WI: Implement "
				+ "jump-to methods for nodes in knowledge graph\",\"description\":\"Implement "
				+ "jump-to methods for jira issue nodes, git commit nodes, changed file nodes, "
				+ "code method nodes and decision knowledge element nodes.\",\"type\":\"Work "
				+ "Item\",\"key\":\"ECONDEC-20\",\"url\":\"https:\\/\\/jira-se.ifi.uni-heidelberg.de/browse/ECONDEC-20\"}]";
		List<KnowledgeElement> elements = KnowledgeElement.parseJsonString(jsonString);
		KnowledgeElement element = elements.get(0);
		assertEquals("WI: Implement jump-to methods for nodes in knowledge graph", element.getSummary());
		assertEquals(0, element.getPageId());
		assertEquals("ECONDEC-20", element.getKey());
		assertEquals("https://jira-se.ifi.uni-heidelberg.de/browse/ECONDEC-20", element.getLink());
	}

	@Test
	public void testParseTypes() {
		String jsonString = "[{'type':'issue'}, {'type':'decision'}]";
		List<KnowledgeElement> elements = KnowledgeElement.parseJsonString(jsonString);
		assertEquals("issue", elements.get(0).getType());
		assertEquals("decision", elements.get(1).getType());
	}

	@Test
	public void testParseInvalidJsonString() {
		String jsonString = "[[test]]";
		List<KnowledgeElement> elements = KnowledgeElement.parseJsonString(jsonString);
		assertEquals(0, elements.size());
	}
}