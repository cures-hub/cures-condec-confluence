package de.uhd.ifi.se.decision.management.confluence.oauth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.atlassian.sal.api.component.ComponentLocator;

import de.uhd.ifi.se.decision.management.confluence.mocks.MockComponentLocator;
import de.uhd.ifi.se.decision.management.confluence.model.KnowledgeElement;

public class TestJiraClient {

	private static JiraClient jiraClient;

	@BeforeClass
	public static void setUp() {
		ComponentLocator.setComponentLocator(new MockComponentLocator());
		jiraClient = new JiraClient();
	}

	@Test
	public void testConstructor() {
		assertNotNull(new JiraClient());
	}

	@Test
	public void testGetDecisionKnowledgeFromJira() {
		List<KnowledgeElement> elements = jiraClient.getDecisionKnowledgeFromJira("", "CONDEC", 1585699200, 1604188800,
				new ArrayList<>(), new ArrayList<>());
		assertEquals("issue", elements.get(0).getType());
		assertEquals("decision", elements.get(1).getType());
	}

	@Test
	public void testConvertListToJson() {
		List<String> knowledgeTypes = new ArrayList<>();
		knowledgeTypes.add("Issue");
		knowledgeTypes.add("Decision");
		assertEquals("[\"Issue\",\"Decision\"]", JiraClient.convertToJsonArray(knowledgeTypes));
	}
}
