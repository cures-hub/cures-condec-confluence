package de.uhd.ifi.se.decision.management.confluence.oauth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Set;

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
	public void testGetCurrentActiveJiraProjects() {
		Set<String> projectKeys = jiraClient.getJiraProjects();
		assertEquals(1, projectKeys.size());
	}

	@Test
	public void testGetDecisionKnowledgeFromJira() {
		List<KnowledgeElement> elements = jiraClient.getDecisionKnowledgeFromJira("", "CONDEC", 1585699200, 1604188800);
		assertEquals("issue", elements.get(0).getType());
		assertEquals("decision", elements.get(1).getType());
	}

	@Test
	public void testParseJiraProjectsJsonOneProject() {
		Set<String> projects = jiraClient.parseJiraProjectsJson("CONDEC");
		assertEquals("CONDEC", projects.iterator().next());
	}

	@Test
	public void testParseJiraProjectsJsonManyProjects() {
		Set<String> projects = jiraClient.parseJiraProjectsJson("[ {'key' : 'TEST'}, {'key' : 'CONDEC'} ]");
		assertEquals(2, projects.size());
	}
}
