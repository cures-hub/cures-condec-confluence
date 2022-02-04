package de.uhd.ifi.se.decision.management.confluence.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TestCreateJsonFromObject {

	@Test
	public void testToJsonStringWithValidElements() {
		KnowledgeElement element = new KnowledgeElement();
		element.setSummary("Link new created type to different devices inside the pop up field.");
		element.setLink("https://jira-se.ifi.uni-heidelberg.de/browse/ISE2019-103");
		element.setUpdatingDate("2042-01-01");
		String jsonString = "[{\"description\":\"Link new created type to different devices inside the pop up field.\","
				+ "\"link\":\"https://jira-se.ifi.uni-heidelberg.de/browse/ISE2019-103\","
				+ "\"status\":\"undefined\",\"statusColor\":\"black\","
				+ "\"summary\":\"Link new created type to different devices inside the pop up field.\","
				+ "\"updatingDate\":\"2042-01-01\"}]";

		List<KnowledgeElement> elements = new ArrayList<>();
		elements.add(element);
		assertEquals(jsonString, KnowledgeElement.toJsonString(elements));
	}

	@Test
	public void testToJsonStringWithInvalidElements() {
		KnowledgeElement element = new KnowledgeElement();
		List<KnowledgeElement> elements = new ArrayList<>();
		elements.add(element);
		assertEquals("", KnowledgeElement.toJsonString(elements));
	}
}