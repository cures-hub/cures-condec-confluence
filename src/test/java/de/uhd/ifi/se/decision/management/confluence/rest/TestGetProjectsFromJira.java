package de.uhd.ifi.se.decision.management.confluence.rest;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;

import com.atlassian.sal.api.component.ComponentLocator;
import com.sun.jersey.api.client.ClientResponse.Status;

import de.uhd.ifi.se.decision.management.confluence.mocks.MockComponentLocator;

public class TestGetProjectsFromJira {
	private KnowledgeRest knowledgeRest;

	@Before
	public void setUp() {
		ComponentLocator.setComponentLocator(new MockComponentLocator());
		knowledgeRest = new KnowledgeRest();
	}

	@Test
	public void testValid() {
		Response response = knowledgeRest.getProjectsFromJira();
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
	}
}
